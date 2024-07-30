package org.varukha.deliveryservice.service;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.varukha.deliveryservice.dto.cargo.CargoRequestDto;
import org.varukha.deliveryservice.dto.vehicle.VehicleDto;
import org.varukha.deliveryservice.dto.vehicle.VehicleRequestDto;
import org.varukha.deliveryservice.dto.vehicle.VehicleResponseDto;
import org.varukha.deliveryservice.model.Vehicle;

/**
 * Service interface defining operations related to vehicles.
 */
public interface VehicleService {
    /**
     * Saves a new vehicle based on the provided request DTO.
     *
     * @param requestDto The request DTO containing vehicle information.
     * @return The response DTO representing the saved vehicle.
     */
    VehicleResponseDto save(VehicleRequestDto requestDto);

    /**
     * Retrieves all vehicles with cargos using pagination.
     *
     * @param pageable The pagination information.
     * @return A list of response DTOs representing the vehicles.
     */
    List<VehicleResponseDto> getAll(Pageable pageable);

    /**
     * Updates an existing vehicle based on the provided ID and request DTO.
     *
     * @param id         The ID of the vehicle to update.
     * @param requestDto The request DTO containing updated vehicle information.
     * @return The updated vehicle DTO.
     */
    VehicleDto update(Long id, VehicleRequestDto requestDto);

    /**
     * Retrieves a vehicle by its number from a cargo request DTO.
     *
     * @param requestDto The cargo request DTO containing the vehicle number.
     * @return The retrieved vehicle.
     */
    Vehicle getVehicleByNumber(CargoRequestDto requestDto);

    /**
     * Deletes a vehicle by its ID.
     *
     * @param id The ID of the vehicle to delete.
     */
    void deleteById(Long id);
}
