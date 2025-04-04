package my.hoty.lab3xx.service;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import my.hoty.lab3xx.entity.OperationStatus;
import my.hoty.lab3xx.entity.Vehicle;
import my.hoty.lab3xx.model.Client;
import my.hoty.lab3xx.model.ImportOperation;
import my.hoty.lab3xx.repository.ImportOperationRepo;
import my.hoty.lab3xx.repository.RoleRepo;
import my.hoty.lab3xx.repository.VehicleRepo;
import my.hoty.lab3xx.util.JsonVehicleParser;
import my.hoty.lab3xx.util.VehicleValidator;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class ImportService {
    private final VehicleRepo vehicleRepo;
    private final ClientService clientService;
    private final ImportOperationRepo importOperationRepo;
    private final VehicleValidator validator;
    private final JsonVehicleParser jsonVehicleParser;
    private final RoleRepo roleRepo;
    private final MinioService minioService;

    @Transactional
    public ImportOperation processImport(MultipartFile file, Client initiator) {
        String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
        List<Vehicle> vehicles;
        try {
            vehicles = parseFile(file);
            validateVehicles(vehicles);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse json file", e);
        }

        try (InputStream stream = file.getInputStream()) {
            minioService.uploadFile(filename, stream, file.getSize(), file.getContentType());
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload file", e);
        }
        for (Vehicle vehicle : vehicles) {
            vehicle.setClient(initiator);
            vehicle.setCreationDate(ZonedDateTime.now());
        }
        vehicles = vehicleRepo.saveAll(vehicles);
        ImportOperation importOperation = new ImportOperation();
        try {
            importOperation.setFilename(filename);
            importOperation.setTimestamp(LocalDateTime.now());
            importOperation.setFileUrl(minioService.getFileUrl(filename));
            importOperation.setImportedBy(initiator);
            importOperation.setImportedCount(vehicles.size());
            importOperation.setStatus(OperationStatus.SUCCESS);
        } catch (Exception e) {
            importOperation.setStatus(OperationStatus.FAILURE);
            throw new RuntimeException("Failed to load file url", e);
        }
        importOperationRepo.save(importOperation);

        return importOperation;
    }

    public List<ImportOperation> getImportHistory(Client client) {
        if (client.isAdmin()) {
            return importOperationRepo.findAll(Sort.by(Sort.Direction.DESC, "timestamp"));
        } else {
            return importOperationRepo.findByImportedBy(client, Sort.by(Sort.Direction.DESC, "timestamp"));
        }
    }

    private List<Vehicle> parseFile(MultipartFile file) throws IOException {
        String content = new String(file.getBytes(), StandardCharsets.UTF_8);
        if (Objects.equals(file.getContentType(), "application/json")) {
            return jsonVehicleParser.parseJson(content);
        }
        else {
            throw new IllegalArgumentException("Invalid content type");
        }
    }

    private void validateVehicles(List<Vehicle> vehicles) {
        for (Vehicle vehicle : vehicles) {
            Errors errors = new BeanPropertyBindingResult(vehicle, "vehicle");
            validator.validate(vehicle, errors);
            if (errors.hasErrors()) {
                throw new ValidationException("Validation failed: " + errors.getAllErrors());
            }
        }
    }

}
