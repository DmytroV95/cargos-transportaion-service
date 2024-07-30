package org.varukha.deliveryservice.dto.vehicle;

import java.util.List;
import org.varukha.deliveryservice.dto.cargo.CargoDto;
import org.varukha.deliveryservice.model.enums.VehicleType;

/**
 * A data transfer object (DTO) representing a response containing information about a vehicle.
 * Includes details such as the vehicle ID, type, number, route from and to, and a list
 * of cargos associated with the vehicle.
 */
public record VehicleResponseDto(
        Long id,
        VehicleType type,
        String vehicleNumber,
        String routeFrom,
        String routeTo,
        List<CargoDto> cargos) {
}
