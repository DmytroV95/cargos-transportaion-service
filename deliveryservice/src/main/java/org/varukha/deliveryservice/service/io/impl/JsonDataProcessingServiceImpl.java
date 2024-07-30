package org.varukha.deliveryservice.service.io.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.varukha.deliveryservice.dto.DataProcessingResponseDto;
import org.varukha.deliveryservice.model.Cargo;
import org.varukha.deliveryservice.repository.CargoRepository;
import org.varukha.deliveryservice.service.io.JsonDataProcessingService;
import org.varukha.deliveryservice.service.io.JsonReaderService;

/**
 * Service class for processing JSON data from uploaded files.
 */
@Service
@Log4j2
@RequiredArgsConstructor
public class JsonDataProcessingServiceImpl implements JsonDataProcessingService {
    private static final String JSON_RESPONSE_PATH = "data_processing_response/response.json";
    private static final Integer THREAD_NUMBER = Runtime.getRuntime().availableProcessors();

    private final JsonReaderService jsonReaderService;
    private final CargoRepository cargoRepository;
    private final ObjectMapper objectMapper;

    /**
     * Uploads JSON files and processes them, returning a response DTO.
     *
     * @param files List of Multipart files containing JSON data
     * @return DataProcessingResponseDto containing import statistics
     */
    @Override
    public DataProcessingResponseDto uploadFromJson(List<MultipartFile> files) {
        DataProcessingResponseDto responseDto = new DataProcessingResponseDto(0, 0);
        try {
            List<DataProcessingResponseDto> responseDtoList = processFiles(files);
            responseDto = calculateTotalImports(responseDtoList);
            writeResponseToFile(responseDto);
        } catch (RuntimeException e) {
            log.error("Error occurred while saving data"
                    + " from JSON files: " + e.getMessage());
        }
        return responseDto;
    }

    /**
     * Processes a list of Multipart files containing JSON data concurrently.
     *
     * @param files List of Multipart files containing JSON data
     * @return List of DataProcessingResponseDto for each processed file
     */
    private List<DataProcessingResponseDto> processFiles(List<MultipartFile> files) {
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_NUMBER);
        CompletionService<DataProcessingResponseDto> completionService =
                new ExecutorCompletionService<>(executor);
        List<Future<DataProcessingResponseDto>> futures = new ArrayList<>();
        try {
            for (MultipartFile file : files) {
                log.info("Processing file: {}", file.getOriginalFilename());
                futures.add(completionService.submit(() -> {
                    List<Cargo> cargoList = jsonReaderService.loadJsonFile(file);
                    return processCargoList(cargoList);
                }));
            }
            List<DataProcessingResponseDto> responseDtoList = new ArrayList<>();
            for (Future<DataProcessingResponseDto> future : futures) {
                try {
                    responseDtoList.add(future.get());
                } catch (InterruptedException | ExecutionException e) {
                    log.error("Error occurred while processing files: {}", e.getMessage());
                }
            }
            return responseDtoList;
        } finally {
            if (executor != null) {
                waitForCompletion(executor);
            }
        }
    }

    /**
     * Method to wait for the completion of all threads in the executor service.
     *
     * @param executorService The executor service to wait for completion
     */
    private void waitForCompletion(ExecutorService executorService) {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(10, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
                if (!executorService.awaitTermination(10, TimeUnit.SECONDS)) {
                    log.error("Executor service did not terminate after shutdownNow()");
                }
            }
        } catch (InterruptedException e) {
            log.error("Thread interrupted while waiting for executor service termination");
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Calculates the total number of successful and failed imports from a list of DTOs.
     *
     * @param dtoList List of DataProcessingResponseDto
     * @return Total import statistics as a single DataProcessingResponseDto
     */
    private DataProcessingResponseDto calculateTotalImports(
            List<DataProcessingResponseDto> dtoList) {
        int totalSuccessfulImports = 0;
        int totalFailedImports = 0;
        for (DataProcessingResponseDto responseDto : dtoList) {
            totalSuccessfulImports += responseDto.successfulImports();
            totalFailedImports += responseDto.failedImports();
        }
        return new DataProcessingResponseDto(totalSuccessfulImports, totalFailedImports);
    }

    /**
     * Processes the cargo list, saving cargo data to the database and returning a response DTO.
     *
     * @param cargoList List of Cargo objects to process
     * @return DataProcessingResponseDto containing import statistics
     */
    private DataProcessingResponseDto processCargoList(List<Cargo> cargoList) {
        int successfulImports = 0;
        int failedImports = 0;
        for (Cargo cargo : cargoList) {
            try {
                if (cargo.getVehicle() != null) {
                    cargoRepository.save(cargo);
                    log.info("Cargo saved successfully: id={}, vehicle={}, description={}",
                            cargo.getId(), cargo.getVehicle(), cargo.getDescription());
                    successfulImports++;
                } else {
                    log.warn("Cargo's vehicle is null. Skipping saving cargo data.");
                    failedImports++;
                }
            } catch (Exception e) {
                String errorMessage = "Error saving cargo data: " + e.getMessage();
                log.error(errorMessage);
                throw new RuntimeException(errorMessage, e);
            }
        }
        return new DataProcessingResponseDto(successfulImports, failedImports);
    }

    /**
     * Writes the response DTO to a JSON file.
     *
     * @param responseDto DataProcessingResponseDto to write
     */
    public void writeResponseToFile(DataProcessingResponseDto responseDto) {
        try {
            Files.createDirectories(Paths.get(JSON_RESPONSE_PATH).getParent());
            File file = new File(JSON_RESPONSE_PATH);
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
            objectMapper.writeValue(file, responseDto);
            log.info("Report generated successfully."
                    + " Please follow link: " + file.getAbsolutePath());
        } catch (IOException e) {
            log.error("Error writing response to file: {}", e.getMessage());
            throw new RuntimeException("Failed to write response to file", e);
        }
    }
}
