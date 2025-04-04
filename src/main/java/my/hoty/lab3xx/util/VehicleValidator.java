package my.hoty.lab3xx.util;

import my.hoty.lab3xx.entity.Vehicle;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class VehicleValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return Vehicle.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Vehicle vehicle = (Vehicle) target;
        if (vehicle.getName() == null || vehicle.getName().isEmpty()) {
            errors.rejectValue("name", "NotEmpty", "Vehicle name is required");
        }
        if (vehicle.getCoordinates() == null) {
            errors.rejectValue("coordinates", "NotEmpty", "Coordinate is required");
        }
        if (vehicle.getType() == null) {
            errors.rejectValue("type", "NotEmpty", "Type is required");
        }
        if (vehicle.getEnginePower() == null || vehicle.getEnginePower() <= 0) {
            errors.rejectValue("enginePower", "NotEmpty", "Engine power is required");
        }
        if (vehicle.getNumberOfWheels() < 0) {
            errors.rejectValue("numberOfWheels", "NotEmpty", "Number of wheels is required");
        }
        if (vehicle.getDistanceTravelled() < 0) {
            errors.rejectValue("numberOfWheels", "NotEmpty", "Distance travelled should be greater than 0");
        }
        if (vehicle.getFuelConsumption() == null || vehicle.getFuelConsumption() <= 0) {
            errors.rejectValue("fuelConsumption", "NotEmpty", "Fuel consumption is required");
        }
        if (vehicle.getFuelType() == null) {
            errors.rejectValue("fuelType", "NotEmpty", "Fuel type is required");
        }
    }
}
