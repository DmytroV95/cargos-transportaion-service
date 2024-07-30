package org.varukha.deliveryservice.dto.vehicle;

/**
 * A data transfer object (DTO) representing information about a vehicle.
 * Includes the type of the vehicle.
 */
public record VehicleInfoDto(String type,
                             String vehicleNumber) {}
