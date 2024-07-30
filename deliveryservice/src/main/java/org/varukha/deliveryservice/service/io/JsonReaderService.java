package org.varukha.deliveryservice.service.io;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;
import org.varukha.deliveryservice.model.Cargo;

/**
 * Service for reading JSON files and loading cargo data.
 */
public interface JsonReaderService {
    /**
     * Loads JSON file into a list of Cargo objects.
     *
     * @param file the JSON file to parse
     * @return a list of Cargo objects parsed from the JSON file
     * @throws RuntimeException if the specified file is not a valid JSON file
     */
    List<Cargo> loadJsonFile(MultipartFile file);
}
