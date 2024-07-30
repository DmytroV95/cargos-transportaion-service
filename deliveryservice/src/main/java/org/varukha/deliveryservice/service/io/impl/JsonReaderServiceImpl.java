package org.varukha.deliveryservice.service.io.impl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.varukha.deliveryservice.model.Cargo;
import org.varukha.deliveryservice.service.io.JsonReaderService;

/**
 * Service for reading JSON files and parsing them into Cargo objects.
 * This class provides a method to load a JSON file
 * and parse its contents into a list of Cargo objects.
 */
@Service
@RequiredArgsConstructor
public class JsonReaderServiceImpl implements JsonReaderService {
    private static final Logger LOGGER = LogManager.getLogger(JsonReaderServiceImpl.class);
    private final ObjectMapper objectMapper;

    /**
     * Loads JSON file into a list of Cargo objects.
     *
     * @param file the JSON file to parse
     * @return a list of Cargo objects parsed from the JSON file
     * @throws RuntimeException if the specified file is not a valid JSON file
     */
    public List<Cargo> loadJsonFile(MultipartFile file) {
        List<Cargo> cargoList = new ArrayList<>();
        try (JsonParser parser = createJsonParser(file)) {
            while (parser.nextToken() != null) {
                if (parser.getCurrentToken() == JsonToken.START_OBJECT) {
                    Cargo cargo = objectMapper.readValue(parser, Cargo.class);
                    cargoList.add(cargo);
                }
            }
        } catch (IOException e) {
            LOGGER.error("Error parsing the file {}", file.getName());
            throw new RuntimeException("Error: The specified file "
                    + file.getName() + " is not a valid JSON file.");
        }
        return cargoList;
    }

    /**
     * Creates a JsonParser for the specified file.
     *
     * @param file the JSON file
     * @return a JsonParser instance
     * @throws IOException if an I/O error occurs
     */
    private JsonParser createJsonParser(MultipartFile file) throws IOException {
        InputStream inputStream = file.getInputStream();
        return objectMapper.getFactory().createParser(inputStream);
    }
}
