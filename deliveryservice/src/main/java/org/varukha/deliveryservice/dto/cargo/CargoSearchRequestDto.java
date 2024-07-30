package org.varukha.deliveryservice.dto.cargo;

/**
 * A data transfer object (DTO) representing a request for cargo search operations.
 * Contains arrays specifying the types and statuses of cargos to search for.
 */
public record CargoSearchRequestDto(
        String[] type,
        String[] status) {
}
