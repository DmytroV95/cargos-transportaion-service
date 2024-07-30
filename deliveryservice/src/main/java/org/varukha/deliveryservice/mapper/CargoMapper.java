package org.varukha.deliveryservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.varukha.deliveryservice.config.MapperConfig;
import org.varukha.deliveryservice.dto.cargo.CargoRequestDto;
import org.varukha.deliveryservice.dto.cargo.CargoResponseDto;
import org.varukha.deliveryservice.dto.cargo.CargoSearchResponseDto;
import org.varukha.deliveryservice.dto.vehicle.VehicleInfoDto;
import org.varukha.deliveryservice.model.Cargo;
import org.varukha.deliveryservice.model.Vehicle;

/**
 * Mapper interface for mapping cargo-related objects.
 */
@Mapper(config = MapperConfig.class)
public interface CargoMapper {
    /**
     * Converts a Cargo entity to a CargoResponseDto.
     *
     * @param cargo the Cargo entity to convert.
     * @return the corresponding CargoResponseDto.
     */
    CargoResponseDto toCargoResponseDto(Cargo cargo);

    /**
     * Converts a CargoRequestDto to a Cargo entity.
     *
     * @param requestDto the CargoRequestDto to convert.
     * @return the corresponding Cargo entity.
     */
    @Mapping(target = "status", source = "status")
    Cargo toModel(CargoRequestDto requestDto);

    /**
     * Converts a Cargo entity to a CargoSearchResponseDto.
     *
     * @param cargo the Cargo entity to convert.
     * @return the corresponding CargoSearchResponseDto.
     */
    @Mapping(target = "vehicle", source = "cargo.vehicle",
            qualifiedByName = "mapToVehicleInfo")
    CargoSearchResponseDto toCargoSearchResponseDto(Cargo cargo);

    /**
     * Maps a Vehicle entity to a VehicleInfoDto.
     *
     * @param vehicle the Vehicle entity to map.
     * @return the corresponding VehicleInfoDto.
     */
    @Named("mapToVehicleInfo")
    static VehicleInfoDto mapVehicleToVehicleInfo(Vehicle vehicle) {
        return new VehicleInfoDto(
                vehicle.getType().getVehicleType(),
                vehicle.getVehicleNumber());
    }
}
