package org.varukha.deliveryservice.dto.cargo;

import java.util.List;

/**
 * A data transfer object (DTO) representing a response for a list of cargo items.
 * Contains a list of cargo search response DTOs, along with information about
 * the current page number and total pages.
 */
public record CargoListResponseDto(
        List<CargoSearchResponseDto> list,
        Integer currentPageNumber,
        Integer totalPages) {
}
