package org.varukha.deliveryservice.dto.vehicle;

import org.varukha.deliveryservice.model.enums.VehicleType;

/**
 * A data transfer object (DTO) representing a vehicle.
 * Contains details such as the vehicle ID, type, number, route from, and route to.
 */
public record VehicleDto(
        Long id,
        VehicleType type,
        String vehicleNumber,
        String routeFrom,
        String routeTo) {
}
