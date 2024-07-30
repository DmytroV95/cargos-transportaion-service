package org.varukha.deliveryservice.dto.vehicle;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.varukha.deliveryservice.model.enums.VehicleType;

/**
 * A data transfer object (DTO) representing a request for creating or updating a vehicle.
 * Contains details such as the vehicle type, number, route from, and route to.
 */
public record VehicleRequestDto(
        @NotNull(message = "Type must not be null")
        VehicleType type,

        @NotBlank(message = "Vehicle number must not be blank")
        @Pattern(regexp = "^[A-Za-z0-9]+$",
                message = "Vehicle number must contain only alphanumeric characters")
        String vehicleNumber,

        @NotBlank(message = "Route from must not be blank")
        String routeFrom,

        @NotBlank(message = "Route to must not be blank")
        String routeTo) {
}
