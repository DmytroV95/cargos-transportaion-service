package org.varukha.deliveryservice.service.io;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;
import org.varukha.deliveryservice.dto.DataProcessingResponseDto;

/**
 * Service for uploading JSON files and processing them.
 */
public interface JsonDataProcessingService {
    /**
     * Uploads JSON files and processes them, returning a response DTO.
     *
     * @param files List of Multipart files containing JSON data
     * @return DataProcessingResponseDto containing import statistics
     */
    DataProcessingResponseDto uploadFromJson(List<MultipartFile> files);
}
