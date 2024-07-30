package org.varukha.deliveryservice.exception;

import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;

/**
 * A data transfer object (DTO) representing an error response.
 * Contains information about the timestamp when the error occurred,
 * the HTTP status code, and an array of error messages.
 */
public record ErrorResponseDto(LocalDateTime timestamp,
                               HttpStatus status,
                               String[] errors){
}
