package org.varukha.deliveryservice.dto;

/**
 * A data transfer object (DTO) representing the response of data processing operations.
 * Contains information about the number of successful imports and failed imports.
 */
public record DataProcessingResponseDto(
        Integer successfulImports,
        Integer failedImports) {
}
