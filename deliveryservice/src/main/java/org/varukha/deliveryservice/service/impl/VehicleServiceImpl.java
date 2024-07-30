package org.varukha.deliveryservice.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.varukha.deliveryservice.dto.cargo.CargoRequestDto;
import org.varukha.deliveryservice.dto.vehicle.VehicleDto;
import org.varukha.deliveryservice.dto.vehicle.VehicleRequestDto;
import org.varukha.deliveryservice.dto.vehicle.VehicleResponseDto;
import org.varukha.deliveryservice.exception.EntityNotFoundException;
import org.varukha.deliveryservice.mapper.VehicleMapper;
import org.varukha.deliveryservice.model.Vehicle;
import org.varukha.deliveryservice.repository.VehicleRepository;
import org.varukha.deliveryservice.service.VehicleService;

/**
 * Service class implementing operations related to vehicles.
 */
@Service
@RequiredArgsConstructor
public class VehicleServiceImpl implements VehicleService {
    private final VehicleMapper vehicleMapper;
    private final VehicleRepository vehicleRepository;

    /**
     * Saves a new vehicle based on the provided request DTO.
     *
     * @param requestDto The request DTO containing vehicle information.
     * @return The response DTO representing the saved vehicle.
     */
    @Override
    public VehicleResponseDto save(VehicleRequestDto requestDto) {
        Vehicle vehicle = createVehicle(requestDto);
        return vehicleMapper.toDto(vehicle);
    }

    /**
     * Retrieves all vehicles with cargos using pagination.
     *
     * @param pageable The pagination information.
     * @return A list of response DTOs representing the vehicles.
     */
    @Override
    public List<VehicleResponseDto> getAll(Pageable pageable) {
        return vehicleRepository.findAllWithCargos(pageable)
                .stream()
                .map(vehicleMapper::toDto)
                .toList();
    }

    /**
     * Updates an existing vehicle based on the provided ID and request DTO.
     *
     * @param id         The ID of the vehicle to update.
     * @param requestDto The request DTO containing updated vehicle information.
     * @return The updated vehicle DTO.
     */
    @Override
    public VehicleDto update(Long id, VehicleRequestDto requestDto) {
        Vehicle updatedVehicle = vehicleMapper.toModel(requestDto);
        updatedVehicle.setId(id);
        Vehicle savedVehicle = vehicleRepository.save(updatedVehicle);
        return vehicleMapper.toVehicleDto(savedVehicle);
    }

    /**
     * Retrieves a vehicle by its number from a cargo request DTO.
     *
     * @param requestDto The cargo request DTO containing the vehicle number.
     * @return The retrieved vehicle.
     * @throws EntityNotFoundException if the vehicle is not found.
     */
    @Override
    public Vehicle getVehicleByNumber(CargoRequestDto requestDto) {
        return vehicleRepository.findByVehicleNumber(requestDto.vehicleNumber())
                .orElseThrow(() -> new EntityNotFoundException("Can't find vehicle by"
                        + " vehicle number: " + requestDto.vehicleNumber())
                );
    }

    /**
     * Deletes a vehicle by its ID.
     *
     * @param id The ID of the vehicle to delete.
     */
    @Override
    public void deleteById(Long id) {
        vehicleRepository.deleteById(id);
    }

    /**
     * Creates a new vehicle based on the provided request DTO.
     *
     * @param requestDto The request DTO containing vehicle information.
     * @return The created vehicle.
     */
    private Vehicle createVehicle(VehicleRequestDto requestDto) {
        Vehicle vehicle = new Vehicle();
        vehicle.setType(requestDto.type());
        vehicle.setVehicleNumber(requestDto.vehicleNumber());
        vehicle.setRouteTo(requestDto.routeTo());
        vehicle.setRouteFrom(requestDto.routeFrom());
        vehicleRepository.save(vehicle);
        return vehicle;
    }
}
