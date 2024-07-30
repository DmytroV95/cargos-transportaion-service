package org.varukha.deliveryservice.dto.cargo;

import org.varukha.deliveryservice.dto.vehicle.VehicleInfoDto;

/**
 * A data transfer object (DTO) representing a response for cargo search operations.
 * Contains details such as the cargo ID, information about the associated vehicle,
 * description, weight, and status of the cargo.
 */
public record CargoSearchResponseDto(
        Long id,
        VehicleInfoDto vehicle,
        String description,
        Double weight,
        String status) {
}
