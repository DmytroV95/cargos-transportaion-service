package org.varukha.deliveryservice.dto.cargo;

/**
 * A data transfer object (DTO) representing information about cargo.
 * Contains details such as the cargo ID, description, weight, and status.
 */
public record CargoDto(
        Long id,
        String description,
        Double weight,
        String status) {
}
