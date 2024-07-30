package org.varukha.deliveryservice.util.jsongenerator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.varukha.deliveryservice.model.Cargo;
import org.varukha.deliveryservice.model.Vehicle;
import org.varukha.deliveryservice.model.enums.DeliveryStatus;
import org.varukha.deliveryservice.model.enums.VehicleType;

/**
 * Utility class for generating JSON data.
 */
public class JsonDataGenerator {
    private static final int NUMBER_JSON_FILES = 10;
    private static final int NUMBER_CARGOES_PER_FILE = 100;
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat(
            "##.##", new DecimalFormatSymbols(Locale.US));
    private static final String JSON_DATA_SET_PATH = "src/main/resources/json_data_set";
    private static final Logger LOGGER = LogManager.getLogger(JsonDataGenerator.class);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static void main(String[] args) {
        generateAndSaveJsonFiles();
    }

    /**
     * Generates and saves JSON files with dummy cargo data.
     */
    private static void generateAndSaveJsonFiles() {
        List<Vehicle> vehicles = generateDummyVehicles();
        for (int i = 0; i < NUMBER_JSON_FILES; i++) {
            String fileName = "cargo_data_" + (i + 1) + ".json";
            List<Cargo> cargoesForFile = generateDummyCargoes(vehicles);
            writeToJsonFile(cargoesForFile, fileName);
        }
    }

    /**
     * Generates a list of dummy cargoes.
     *
     * @param vehicles List of vehicles to assign to cargoes.
     * @return A list of dummy cargoes.
     */
    private static List<Cargo> generateDummyCargoes(List<Vehicle> vehicles) {
        List<Cargo> cargoes = new ArrayList<>();
        for (int i = 0; i < NUMBER_CARGOES_PER_FILE; i++) {
            Cargo cargo = new Cargo();
            cargo.setDescription("Cargo " + (i + 1));
            cargo.setWeight(Double.parseDouble(DECIMAL_FORMAT
                    .format(Math.random() * 100)));
            cargo.setStatus(DeliveryStatus
                    .values()[(int) (Math.random() * DeliveryStatus.values().length)]);
            cargo.setVehicle(vehicles.get(i % vehicles.size()));
            cargoes.add(cargo);
        }
        return cargoes;
    }

    /**
     * Generates a list of dummy vehicles with predefined data.
     *
     * @return A list of dummy vehicles.
     */
    private static List<Vehicle> generateDummyVehicles() {
        List<Vehicle> vehicles = new ArrayList<>();
        vehicles.add(
                createVehicle(1L, VehicleType.CAR, "ABC123", "Origin", "Destination"));
        vehicles.add(
                createVehicle(2L, VehicleType.TRUCK, "DEF456", "Origin", "Destination"));
        vehicles.add(
                createVehicle(3L, VehicleType.TRAIN, "GHI789", "Origin", "Destination"));
        vehicles.add(
                createVehicle(4L, VehicleType.PLANE, "JKL012", "Origin", "Destination"));
        vehicles.add(
                createVehicle(5L, VehicleType.SHIP, "MNO345", "Origin", "Destination"));
        vehicles.add(
                createVehicle(6L, VehicleType.HELICOPTER, "PQR678", "Origin", "Destination"));
        vehicles.add(
                createVehicle(7L, VehicleType.DRONE, "STU901", "Origin", "Destination"));
        vehicles.add(
                createVehicle(8L, VehicleType.CAR, "VWX234", "Origin", "Destination"));
        vehicles.add(
                createVehicle(9L, VehicleType.TRUCK, "YZA567", "Origin", "Destination"));
        vehicles.add(
                createVehicle(10L, VehicleType.TRAIN, "BCD890", "Origin", "Destination"));
        return vehicles;
    }

    /**
     * Creates a vehicle with the given details.
     *
     * @param id        The ID of the vehicle.
     * @param type      The type of the vehicle.
     * @param number    The number of the vehicle.
     * @param routeFrom The origin route of the vehicle.
     * @param routeTo   The destination route of the vehicle.
     * @return The created vehicle.
     */
    private static Vehicle createVehicle(Long id,
                                         VehicleType type,
                                         String number,
                                         String routeFrom,
                                         String routeTo) {
        Vehicle vehicle = new Vehicle();
        vehicle.setId(id);
        vehicle.setType(type);
        vehicle.setVehicleNumber(number);
        vehicle.setRouteFrom(routeFrom);
        vehicle.setRouteTo(routeTo);
        return vehicle;
    }

    /**
     * Writes the given list of cargoes to a JSON file.
     *
     * @param cargoes  List of cargoes to write to the file.
     * @param fileName Name of the JSON file.
     */
    private static void writeToJsonFile(List<Cargo> cargoes, String fileName) {
        String filePath = JSON_DATA_SET_PATH + File.separator + fileName;
        try {
            File outputFile = new File(filePath);
            OBJECT_MAPPER.enable(SerializationFeature.INDENT_OUTPUT);
            OBJECT_MAPPER.writeValue(outputFile, cargoes);
        } catch (IOException e) {
            LOGGER.error("An error occurred while "
                    + "writing JSON to file: {}", e.getMessage());
        }
    }
}
