package my.hoty.lab3xx.controller;

import my.hoty.lab3xx.entity.Coordinates;
import my.hoty.lab3xx.entity.FuelType;
import my.hoty.lab3xx.entity.Vehicle;
import my.hoty.lab3xx.entity.VehicleType;
import my.hoty.lab3xx.model.Client;
import my.hoty.lab3xx.repository.VehicleRepo;
import my.hoty.lab3xx.service.ClientService;
import my.hoty.lab3xx.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;

@Controller
public class VehicleController {
    @Autowired
    private VehicleRepo vehicleRepo;

    @Autowired
    private VehicleService vehicleService;

    @Autowired
    private ClientService clientService;

    @RequestMapping("/home/deleteVehicle")
    public String deleteVehicle(Model model, @RequestParam int vehicleId) {
        Vehicle vehicle = vehicleService.findById(vehicleId);
        if(!isOwner(vehicle)) {
            return "redirect:/home/vehicles";
        }
        vehicleService.deleteById(vehicleId);
        return "redirect:/home/vehicles";
    }

    @RequestMapping("/home/updateVehicle")
    public String updateVehicle(Model model, @RequestParam int vehicleId, Principal principal) {
        Vehicle vehicle = vehicleService.findById(vehicleId);
        if(!isOwner(vehicle)) {
            return "redirect:/home/vehicles";
        }
        model.addAttribute("vehicle", vehicle);
        model.addAttribute("client", clientService.findByUsername(principal.getName()));
        return "/home/newVehicle";
    }

    @RequestMapping("/home/filter")
    public String filterVehicle(Model model, @RequestParam String field, @RequestParam String filter, @RequestParam int sort, Principal principal) {
        List<Vehicle> vehicles = vehicleService.findAll();

        vehicles = vehicles.stream()
                .filter(vehicle -> !Objects.equals(field, "Name") || vehicle.getName().contains(filter))
                .filter(vehicle -> !Objects.equals(field, "Type") || vehicle.getType().toString().contains(filter))
                .filter(vehicle -> !Objects.equals(field, "Engine Power") || vehicle.getEnginePower().equals(Float.parseFloat(filter)))
                .filter(vehicle -> !Objects.equals(field, "Number of Wheels") || vehicle.getNumberOfWheels() == Integer.parseInt(filter))
                .filter(vehicle -> !Objects.equals(field, "Capacity") || vehicle.getCapacity() == Double.parseDouble(filter))
                .filter(vehicle -> !Objects.equals(field, "Distance Travelled") || vehicle.getDistanceTravelled() == Double.parseDouble(filter))
                .filter(vehicle -> !Objects.equals(field, "Fuel Consumption") || vehicle.getFuelConsumption() == Float.parseFloat(filter))
                .filter(vehicle -> !Objects.equals(field, "Fuel Type") || vehicle.getFuelType().toString().contains(filter))
                .filter(vehicle -> !Objects.equals(field, "Coord X") || vehicle.getCoordinates().getX().equals(Long.parseLong(filter)))
                .filter(vehicle -> !Objects.equals(field, "Coord Y") || vehicle.getCoordinates().getY().equals(Long.parseLong(filter)))

                .sorted((vehicle1, vehicle2) -> (sort == 0 || Objects.equals(field, "Id")) ? vehicle1.getId().compareTo(vehicle2.getId()) * sort : 0)
                .sorted((vehicle1, vehicle2) -> (sort == 0 || Objects.equals(field, "Name")) ? vehicle1.getName().compareTo(vehicle2.getName()) * sort : 0)
                .sorted((vehicle1, vehicle2) -> (sort == 0 || Objects.equals(field, "Creation Date")) ? vehicle1.getCreationDate().compareTo(vehicle2.getCreationDate()) * sort : 0)
                .sorted((vehicle1, vehicle2) -> (sort == 0 || Objects.equals(field, "Type")) ? vehicle1.getType().compareTo(vehicle2.getType()) * sort : 0)
                .sorted((vehicle1, vehicle2) -> (sort == 0 || Objects.equals(field, "Engine Power")) ? vehicle1.getEnginePower().compareTo(vehicle2.getEnginePower()) * sort : 0)
                .sorted((vehicle1, vehicle2) -> (sort == 0 || Objects.equals(field, "Number of Wheels")) ? Integer.compare(vehicle1.getNumberOfWheels(), vehicle2.getNumberOfWheels()) * sort : 0)
                .sorted((vehicle1, vehicle2) -> (sort == 0 || Objects.equals(field, "Capacity")) ? Double.compare(vehicle1.getCapacity(), vehicle2.getCapacity()) * sort : 0)
                .sorted((vehicle1, vehicle2) -> (sort == 0 || Objects.equals(field, "Distance Travelled")) ? Double.compare(vehicle1.getDistanceTravelled(), vehicle2.getDistanceTravelled()) * sort : 0)
                .sorted((vehicle1, vehicle2) -> (sort == 0 || Objects.equals(field, "Fuel Consumption")) ? Float.compare(vehicle1.getFuelConsumption(), vehicle2.getFuelConsumption()) * sort : 0)
                .sorted((vehicle1, vehicle2) -> (sort == 0 || Objects.equals(field, "Fuel Type")) ? vehicle1.getFuelType().compareTo(vehicle2.getFuelType()) * sort : 0)
                .sorted((vehicle1, vehicle2) -> (sort == 0 || Objects.equals(field, "Coord X")) ? vehicle1.getCoordinates().getX().compareTo(vehicle2.getCoordinates().getX()) * sort : 0)
                .sorted((vehicle1, vehicle2) -> (sort == 0 || Objects.equals(field, "Coord Y")) ? vehicle1.getCoordinates().getY().compareTo(vehicle2.getCoordinates().getY()) * sort : 0)
                .toList();

        model.addAttribute("field", field);
        model.addAttribute("filter", filter);
        model.addAttribute("sort", sort);
        model.addAttribute("vehicles", vehicles);
        model.addAttribute("vehicleService", vehicleService);
        model.addAttribute("client", clientService.findByUsername(principal.getName()));
        model.addAttribute("isAdmin", isAdmin());
        return "/home/vehicles";
    }

    @GetMapping("/home/newVehicle")
    public String newVehicle(Model model, Principal principal) {
        Vehicle vehicle = getVehicle();
        model.addAttribute("vehicle", vehicle);
        model.addAttribute("client", clientService.findByUsername(principal.getName()));
        return "/home/newVehicle";
    }

    @PostMapping("/home/newVehicle")
    public String newVehicle(@ModelAttribute("vehicle") Vehicle vehicle, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        if (vehicle.getId() != null && vehicleService.findById(vehicle.getId()) != null) {
            vehicle.setClient(vehicleService.findById(vehicle.getId()).getClient());
        } else {
            Client client = clientService.findByUsername(username);
            vehicle.setClient(client);
            vehicle.setCreationDate(ZonedDateTime.now());
        }

        vehicleRepo.save(vehicle);
        return "redirect:/home/vehicles";
    }

    @GetMapping("/home/vehicles")
    public String vehicles(Model model, Principal principal) {
        Client client = clientService.findByUsername(principal.getName());
        List<Vehicle> vehicles = vehicleRepo.findAll();
        if(model.getAttribute("vehicles") == null) {
            model.addAttribute("vehicles", vehicles);
        }
        model.addAttribute("client", client);
        model.addAttribute("isAdmin", isAdmin());
        model.addAttribute("vehicleService", vehicleService);
        return "/home/vehicles";
    }

    @RequestMapping("/home/deleteByFuelType")
    public String byFuelType(Model model, @RequestParam FuelType fuelType) {
        vehicleService.deleteByFuelType(fuelType);
        return "redirect:/home/vehicles";
    }

    @GetMapping("/home/byEnginePower")
    public String byEnginePower(Model model, @RequestParam Float min, @RequestParam Float max) {
        model.addAttribute("vehicles", vehicleService.findAll());
        model.addAttribute("vehiclesEngine", vehicleService.findByEnginePowerRange(min, max));
        model.addAttribute("vehicleService", vehicleService);
        model.addAttribute("client", clientService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()));
        model.addAttribute("isAdmin", isAdmin());
        return "/home/vehicles";
    }

    @PostMapping("/home/addWheels")
    public String addWheels(@RequestParam Long id, @RequestParam Integer wheelsToAdd) {
        vehicleService.addWheels(id, wheelsToAdd);
        return "redirect:/home/vehicles";
    }

    private boolean isOwner(Vehicle vehicle) {
        if(vehicle == null) {
            return false;
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Client client = clientService.findByUsername(username);
        return client.getId().equals(vehicle.getClient().getId()) || isAdmin();
    }

    private boolean isAdmin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        return (auth != null && auth.getAuthorities().stream()
                .anyMatch(g -> g.getAuthority().equals("ADMIN")));
    }

    private static Vehicle getVehicle() {
        Coordinates coordinates = new Coordinates();
        coordinates.setX(13L);
        coordinates.setY(14L);
        Vehicle vehicle = new Vehicle();
        vehicle.setName("test");
        vehicle.setCapacity(0.5);
        vehicle.setCoordinates(coordinates);
        vehicle.setType(VehicleType.BICYCLE);
        vehicle.setEnginePower(333.3F);
        vehicle.setDistanceTravelled(432.6);
        vehicle.setFuelConsumption(4.1F);
        vehicle.setFuelType(FuelType.NUCLEAR);
        vehicle.setNumberOfWheels(2);
        vehicle.setCreationDate(ZonedDateTime.now());

        return vehicle;
    }
}
