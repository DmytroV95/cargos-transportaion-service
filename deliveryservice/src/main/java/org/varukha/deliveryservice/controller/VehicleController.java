package org.varukha.deliveryservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.varukha.deliveryservice.dto.vehicle.VehicleDto;
import org.varukha.deliveryservice.dto.vehicle.VehicleRequestDto;
import org.varukha.deliveryservice.dto.vehicle.VehicleResponseDto;
import org.varukha.deliveryservice.service.VehicleService;

@Tag(name = "Vehicle management",
        description = "Endpoints for managing vehicles")
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/vehicles")
public class VehicleController {
    private final VehicleService vehicleService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Save vehicle",
            description = "Save new vehicle no database")
    public VehicleResponseDto save(@RequestBody VehicleRequestDto requestDto) {
        return vehicleService.save(requestDto);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get all vehicles",
            description = "Get list of all vehicles")
    public List<VehicleResponseDto> getAll(Pageable pageable) {
        return vehicleService.getAll(pageable);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update the vehicle by ID",
            description = "Update the existing vehicle data by ID")
    public VehicleDto update(@PathVariable Long id,
                             @Valid @RequestBody VehicleRequestDto requestDto) {
        return vehicleService.update(id, requestDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete the vehicle by id",
            description = "Delete the existing vehicle by ID")
    public void deleteById(@PathVariable @NotNull @Positive(
            message = "ID must be a positive non-zero value") Long id) {
        vehicleService.deleteById(id);
    }
}
