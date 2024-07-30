package org.varukha.deliveryservice.dto.cargo;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.varukha.deliveryservice.model.enums.DeliveryStatus;

/**
 * A data transfer object (DTO) representing a request for cargo operations.
 * Contains details such as the vehicle number, description, weight, and status of the cargo.
 */
public record CargoRequestDto(
        @NotBlank(message = "Vehicle number must not be blank")
        @Pattern(regexp = "^[A-Za-z0-9]+$",
                message = "Vehicle number must contain only alphanumeric characters")
        String vehicleNumber,

        @NotBlank(message = "Description must not be blank")
        String description,

        @NotNull(message = "Weight must not be null")
        @Min(value = 0, message = "Weight must be greater than or equal to 0")
        Double weight,

        @NotNull(message = "Status must not be null")
        DeliveryStatus status) {
}
