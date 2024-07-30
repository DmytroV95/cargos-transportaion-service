package org.varukha.deliveryservice.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.varukha.deliveryservice.dto.cargo.CargoResponseDto;
import org.varukha.deliveryservice.dto.cargo.CargoSearchRequestDto;
import org.varukha.deliveryservice.service.CargoExcelReportService;
import org.varukha.deliveryservice.service.CargoService;

/**
 * Service implementation for generating Excel reports for cargo entities.
 */
@Service
@RequiredArgsConstructor
public class CargoExcelReportServiceImpl implements CargoExcelReportService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CargoExcelReportServiceImpl.class);
    private static final String REPORT_SHEET_NAME_CARGO = "Cargos";
    private static final String ID_FIELD = "CargoID";
    private static final String TYPE_FIELD_NAME = "Vehicle type";
    private static final String NUMBER_FIELD_NAME = "Vehicle Number";
    private static final String ROUTE_FROM_FIELD_NAME = "Delivery route from";
    private static final String ROUTE_TO_FIELD_NAME = "Delivery route to";
    private static final String DESCRIPTION_FIELD_NAME = "Cargo Description";
    private static final String WEIGHT_FIELD_NAME = "Cargo Weight";
    private static final String STATUS_FIELD_NAME = "Delivery Status";
    private static final int FIRST_ROW_INDEX_HEADER = 0;
    private static final int ID_CELL_INDEX_HEADER = 0;
    private static final int TYPE_CELL_INDEX_HEADER = 1;
    private static final int NUMBER_CELL_INDEX_HEADER = 2;
    private static final int ROUTE_FROM_CELL_INDEX_HEADER = 3;
    private static final int ROUTE_TO_CELL_INDEX_HEADER = 4;
    private static final int DESCRIPTION_CELL_INDEX_HEADER = 5;
    private static final int WEIGHT_CELL_INDEX_HEADER = 6;
    private static final int STATUS_CELL_INDEX_HEADER = 7;
    private static final String RESPONSE_HEADER_ATTACHMENT = "attachment";
    private static final String REPORT_NAME_HEADER = "cargos_report.xlsx";

    private final CargoService cargoService;

    /**
     * Generates and returns an Excel report for cargos based on the provided search parameters.
     *
     * @param searchParameters The search parameters for filtering cargos.
     * @return ResponseEntity containing the Excel report.
     */
    @Override
    @Transactional
    public ResponseEntity<Resource> generateExcelReport(CargoSearchRequestDto searchParameters) {
        List<CargoResponseDto> cargoList = cargoService.getFilteredList(searchParameters);
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet(REPORT_SHEET_NAME_CARGO);
        createHeaderRow(sheet);
        populateDataRow(cargoList, sheet);
        autoSizeColumns(sheet);
        byte[] excelBytesArray = getExcelBytesArray(workbook);
        return buildExcelReport(excelBytesArray);
    }

    /**
     * Converts the Excel workbook to a byte array.
     *
     * @param workbook The workbook to convert.
     * @return Byte array containing the Excel workbook.
     * @throws RuntimeException If an error occurs while writing the workbook to the output stream.
     */
    private byte[] getExcelBytesArray(Workbook workbook) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            workbook.write(outputStream);
            return outputStream.toByteArray();
        } catch (IOException e) {
            String errorMessage = "Error writing Excel workbook to"
                    + " output stream: " + e.getMessage();
            LOGGER.error(errorMessage);
            throw new RuntimeException(errorMessage, e);
        } finally {
            try {
                workbook.close();
            } catch (IOException e) {
                LOGGER.error("Error closing workbook: {}", e.getMessage());
            }
        }
    }

    /**
     * Builds the Excel report as a byte array and returns it as a ResponseEntity.
     *
     * @param excelBytes The byte array containing the Excel report.
     * @return ResponseEntity containing the Excel report.
     */
    private ResponseEntity<Resource> buildExcelReport(byte[] excelBytes) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData(RESPONSE_HEADER_ATTACHMENT, REPORT_NAME_HEADER);
        headers.setContentLength(excelBytes.length);

        ByteArrayResource resource = new ByteArrayResource(excelBytes);
        return ResponseEntity.ok()
                .headers(headers)
                .body(resource);
    }

    /**
     * Creates the header row for the Excel report sheet.
     *
     * @param sheet The sheet to create the header row for.
     */
    private void createHeaderRow(Sheet sheet) {
        Row headerRow = sheet.createRow(FIRST_ROW_INDEX_HEADER);
        headerRow.createCell(ID_CELL_INDEX_HEADER).setCellValue(ID_FIELD);
        headerRow.createCell(TYPE_CELL_INDEX_HEADER).setCellValue(TYPE_FIELD_NAME);
        headerRow.createCell(NUMBER_CELL_INDEX_HEADER).setCellValue(NUMBER_FIELD_NAME);
        headerRow.createCell(ROUTE_FROM_CELL_INDEX_HEADER).setCellValue(ROUTE_FROM_FIELD_NAME);
        headerRow.createCell(ROUTE_TO_CELL_INDEX_HEADER).setCellValue(ROUTE_TO_FIELD_NAME);
        headerRow.createCell(DESCRIPTION_CELL_INDEX_HEADER).setCellValue(DESCRIPTION_FIELD_NAME);
        headerRow.createCell(WEIGHT_CELL_INDEX_HEADER).setCellValue(WEIGHT_FIELD_NAME);
        headerRow.createCell(STATUS_CELL_INDEX_HEADER).setCellValue(STATUS_FIELD_NAME);
    }

    /**
     * Populates the data rows in the Excel report sheet with cargo information.
     *
     * @param cargoList The list of cargo response DTOs to populate the data rows with.
     * @param sheet     The sheet to populate with data rows.
     */
    private void populateDataRow(List<CargoResponseDto> cargoList, Sheet sheet) {
        for (int dataRowIndex = 0; dataRowIndex < cargoList.size(); dataRowIndex++) {
            CargoResponseDto cargo = cargoList.get(dataRowIndex);
            Row dataRow = sheet.createRow(dataRowIndex + 1);
            dataRow.createCell(ID_CELL_INDEX_HEADER)
                    .setCellValue(cargo.id());
            dataRow.createCell(TYPE_CELL_INDEX_HEADER)
                    .setCellValue(cargo.vehicle().getType().getVehicleType());
            dataRow.createCell(NUMBER_CELL_INDEX_HEADER)
                    .setCellValue(cargo.vehicle().getVehicleNumber());
            dataRow.createCell(ROUTE_FROM_CELL_INDEX_HEADER)
                    .setCellValue(cargo.vehicle().getRouteFrom());
            dataRow.createCell(ROUTE_TO_CELL_INDEX_HEADER)
                    .setCellValue(cargo.vehicle().getRouteTo());
            dataRow.createCell(DESCRIPTION_CELL_INDEX_HEADER)
                    .setCellValue(cargo.description());
            dataRow.createCell(WEIGHT_CELL_INDEX_HEADER)
                    .setCellValue(cargo.weight());
            dataRow.createCell(STATUS_CELL_INDEX_HEADER)
                    .setCellValue(cargo.status());
        }
    }

    /**
     * Automatically adjusts the width of columns in the Excel report sheet based on the content.
     *
     * @param sheet The sheet to adjust column widths for.
     */
    private void autoSizeColumns(Sheet sheet) {
        Row headerRow = sheet.getRow(FIRST_ROW_INDEX_HEADER);
        if (headerRow != null) {
            int numColumns = headerRow.getLastCellNum();
            for (int i = 0; i < numColumns; i++) {
                sheet.autoSizeColumn(i);
            }
        }
    }
}
