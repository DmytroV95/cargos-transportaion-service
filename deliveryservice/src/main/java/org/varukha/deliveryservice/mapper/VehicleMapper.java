package org.varukha.deliveryservice.mapper;

import org.mapstruct.Mapper;
import org.varukha.deliveryservice.config.MapperConfig;
import org.varukha.deliveryservice.dto.vehicle.VehicleDto;
import org.varukha.deliveryservice.dto.vehicle.VehicleRequestDto;
import org.varukha.deliveryservice.dto.vehicle.VehicleResponseDto;
import org.varukha.deliveryservice.model.Vehicle;

/**
 * Mapper interface for mapping vehicle-related objects.
 */
@Mapper(config = MapperConfig.class)
public interface VehicleMapper {
    /**
     * Converts a Vehicle entity to a VehicleResponseDto.
     *
     * @param vehicle the Vehicle entity to convert.
     * @return the corresponding VehicleResponseDto.
     */
    VehicleResponseDto toDto(Vehicle vehicle);

    /**
     * Converts a Vehicle entity to a VehicleDto.
     *
     * @param vehicle the Vehicle entity to convert.
     * @return the corresponding VehicleDto.
     */
    VehicleDto toVehicleDto(Vehicle vehicle);

    /**
     * Converts a VehicleRequestDto to a Vehicle entity.
     *
     * @param requestDto the VehicleRequestDto to convert.
     * @return the corresponding Vehicle entity.
     */
    Vehicle toModel(VehicleRequestDto requestDto);
}
