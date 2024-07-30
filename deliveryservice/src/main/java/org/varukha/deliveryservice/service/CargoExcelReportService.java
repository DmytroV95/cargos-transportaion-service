package org.varukha.deliveryservice.service;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.varukha.deliveryservice.dto.cargo.CargoSearchRequestDto;

/**
 * Service for generating Excel reports for cargo based on search parameters.
 */
public interface CargoExcelReportService {
    /**
     * Generates an Excel report for cargo based on the provided search parameters.
     *
     * @param searchParameters The search parameters to filter the cargo data.
     * @return ResponseEntity containing the generated Excel report as a Resource.
     */
    ResponseEntity<Resource> generateExcelReport(CargoSearchRequestDto searchParameters);
}
