package my.hoty.lab3xx.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import my.hoty.lab3xx.entity.Coordinates;
import my.hoty.lab3xx.entity.FuelType;
import my.hoty.lab3xx.entity.Vehicle;
import my.hoty.lab3xx.entity.VehicleType;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class JsonVehicleParser {
    private final ObjectMapper mapper;

    public JsonVehicleParser() {
        this.mapper = new ObjectMapper();
        this.mapper.registerModule(new JavaTimeModule());
        this.mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public List<Vehicle> parseJson(String json) {
        try {
            JsonNode rootNode = mapper.readTree(json);

            if(rootNode == null) {
                throw new InvalidFormatException("Invalid JSON");
            }

            List<Vehicle> vehicles = new ArrayList<>();
            for(JsonNode vehicleNode : rootNode) {
                Vehicle vehicle = parseVehicle(vehicleNode);
                vehicles.add(vehicle);
            }

            return vehicles;
        } catch (JsonProcessingException e) {
            throw new InvalidFormatException("Invalid JSON format: " + e.getMessage());
        }
    }

    private Vehicle parseVehicle(JsonNode vehicleNode) {
        Vehicle vehicle = new Vehicle();

        vehicle.setName(getTextValue(vehicleNode, "name"));
        vehicle.setEnginePower(getFloatValue(vehicleNode, "enginePower"));
        vehicle.setType(VehicleType.valueOf(getTextValue(vehicleNode, "type")));
        vehicle.setFuelType(FuelType.valueOf(getTextValue(vehicleNode, "fuelType")));
        vehicle.setFuelConsumption(getFloatValue(vehicleNode, "fuelConsumption"));

        JsonNode coordinatesNode = vehicleNode.get("coordinates");
        if(coordinatesNode != null) {
            Coordinates coordinates = new Coordinates();
            coordinates.setX(getLongValue(coordinatesNode, "x"));
            coordinates.setY(getLongValue(coordinatesNode, "y"));
            vehicle.setCoordinates(coordinates);
        }

        if(vehicleNode.has("numberOfWheels")) {
            vehicle.setNumberOfWheels(vehicleNode.get("numberOfWheels").asInt());
        }
        if(vehicleNode.has("capacity")) {
            vehicle.setCapacity(vehicleNode.get("capacity").asDouble());
        }

        return vehicle;
    }

    // Helper methods for safe parsing
    private String getTextValue(JsonNode node, String field) {
        if (!node.has(field)) {
            throw new InvalidFormatException("Missing required field: " + field);
        }
        return node.get(field).asText();
    }

    private double getDoubleValue(JsonNode node, String field) {
        try {
            return node.get(field).asDouble();
        } catch (Exception e) {
            throw new InvalidFormatException("Invalid double value for field: " + field);
        }
    }

    private float getFloatValue(JsonNode node, String field) {
        try {
            return (float) node.get(field).asDouble();
        } catch (Exception e) {
            throw new InvalidFormatException("Invalid float value for field: " + field);
        }
    }

    private int getIntValue(JsonNode node, String field) {
        try {
            return node.get(field).asInt();
        } catch (Exception e) {
            throw new InvalidFormatException("Invalid integer value for field: " + field);
        }
    }

    private long getLongValue(JsonNode node, String field) {
        try {
            return node.get(field).asLong();
        } catch (Exception e) {
            throw new InvalidFormatException("Invalid long value for field: " + field);
        }
    }
}
