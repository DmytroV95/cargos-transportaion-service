package org.varukha.deliveryservice.dto.cargo;

import org.varukha.deliveryservice.model.Vehicle;

/**
 * A data transfer object (DTO) representing a response for cargo operations.
 * Contains details such as the cargo ID, associated vehicle, description, weight,
 * and status of the cargo.
 */
public record CargoResponseDto(
        Long id,
        Vehicle vehicle,
        String description,
        Double weight,
        String status) {
}
