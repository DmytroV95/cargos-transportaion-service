package org.varukha.deliveryservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.varukha.deliveryservice.dto.DataProcessingResponseDto;
import org.varukha.deliveryservice.dto.cargo.CargoListResponseDto;
import org.varukha.deliveryservice.dto.cargo.CargoRequestDto;
import org.varukha.deliveryservice.dto.cargo.CargoResponseDto;
import org.varukha.deliveryservice.dto.cargo.CargoSearchRequestDto;
import org.varukha.deliveryservice.service.CargoExcelReportService;
import org.varukha.deliveryservice.service.CargoService;
import org.varukha.deliveryservice.service.io.JsonDataProcessingService;

@Tag(name = "Cargo management",
        description = "Endpoints for managing cargos")
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/cargos")
public class CargoController {
    private final CargoService cargoService;
    private final JsonDataProcessingService dataProcessingService;
    private final CargoExcelReportService excelReportService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Save cargo",
            description = "Save new cargo to database")
    public CargoResponseDto save(@Valid @RequestBody CargoRequestDto requestDto) {
        return cargoService.save(requestDto);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get the cargo by ID",
            description = "Get the existing cargo information by ID")
    public CargoResponseDto getById(@PathVariable @NotNull @Positive(
            message = "ID must be a positive non-zero value") Long id) {
        return cargoService.getById(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update the cargo by ID",
            description = "Update the existing cargo data by ID")
    public CargoResponseDto update(@PathVariable @NotNull @Positive(
            message = "ID must be a positive non-zero value") Long id,
                                   @Valid @RequestBody CargoRequestDto requestDto) {
        return cargoService.update(id, requestDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete the cargo by ID",
            description = "Delete the existing cargo by ID")
    public void deleteById(@PathVariable @NotNull @Positive(
            message = "ID must be a positive non-zero value") Long id) {
        cargoService.deleteById(id);
    }

    @GetMapping("/_list")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get cargos by searching parameters",
            description = "Search cargos by input parameters")
    public CargoListResponseDto getCargoList(@RequestParam(defaultValue = "1") int page,
                                             @RequestParam(defaultValue = "10") int size,
                                             CargoSearchRequestDto searchParametersDto) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return cargoService.getPaginatedFilteredList(pageable, searchParametersDto);
    }

    @PostMapping("/file/upload")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Upload JSON files",
            description = "Uploads JSON files and processes them.")
    public ResponseEntity<DataProcessingResponseDto> uploadJsonFile(
            @RequestParam("file") @NotEmpty List<MultipartFile> files) {
        DataProcessingResponseDto responseDto = dataProcessingService.uploadFromJson(files);
        if (responseDto.successfulImports() > 0) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new DataProcessingResponseDto(
                            responseDto.successfulImports(),
                            responseDto.failedImports())
                    );
        } else {
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(null);
        }
    }

    @GetMapping("/_report")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get cargos as Excel file by searching parameters",
            description = "Search cargos by input parameters and return as an Excel file")
    public ResponseEntity<Resource> generateExcelReport(CargoSearchRequestDto searchParameters) {
        return excelReportService.generateExcelReport(searchParameters);
    }
}
