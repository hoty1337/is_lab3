package my.hoty.lab3xx.service;

import jakarta.transaction.Transactional;
import my.hoty.lab3xx.entity.FuelType;
import my.hoty.lab3xx.entity.Vehicle;
import my.hoty.lab3xx.repository.VehicleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class VehicleServiceImpl implements VehicleService {
    @Autowired
    private VehicleRepo vehicleRepo;

    @Override
    public List<Vehicle> findAll() {
        return vehicleRepo.findAll();
    }

    @Override
    public Vehicle findById(int id) {
        return vehicleRepo.findById(id);
    }

    @Override
    public void deleteById(int id) {
        vehicleRepo.deleteById(id);
    }

    public void deleteByFuelType(FuelType fuelType) {
        vehicleRepo.deleteByFuelType(fuelType);
    }

    public Double calculateAverageFuelConsumption() {
        if(vehicleRepo.findAll().isEmpty()) {
            return 0.0;
        }
        return vehicleRepo.calculateAverageFuelConsumption();
    }

    public Map<FuelType, Long> getCountByFuelType() {
        return vehicleRepo.countByFuelType().stream()
                .collect(Collectors.toMap(
                        arr -> (FuelType)arr[0],
                        arr -> (Long)arr[1]));
    }

    public List<Vehicle> findByEnginePowerRange(Float minPower, Float maxPower) {
        return vehicleRepo.findByEnginePowerRange(minPower, maxPower);
    }

    public void addWheels(Long vehicleId, Integer wheelsToAdd) {
        vehicleRepo.addWheels(vehicleId, wheelsToAdd);
    }
}
