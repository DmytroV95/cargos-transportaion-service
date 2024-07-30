package org.varukha.deliveryservice.service;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.varukha.deliveryservice.dto.cargo.CargoListResponseDto;
import org.varukha.deliveryservice.dto.cargo.CargoRequestDto;
import org.varukha.deliveryservice.dto.cargo.CargoResponseDto;
import org.varukha.deliveryservice.dto.cargo.CargoSearchRequestDto;

/**
 * Service interface for managing cargo operations.
 */
public interface CargoService {

    /**
     * Saves a new cargo entity.
     *
     * @param requestDto The cargo data to save.
     * @return The saved cargo response DTO.
     */
    CargoResponseDto save(CargoRequestDto requestDto);

    /**
     * Retrieves a cargo entity by its ID.
     *
     * @param id The ID of the cargo to retrieve.
     * @return The cargo response DTO.
     */
    CargoResponseDto getById(Long id);

    /**
     * Updates an existing cargo entity.
     *
     * @param id         The ID of the cargo to update.
     * @param requestDto The cargo data for the update.
     * @return The updated cargo response DTO.
     */
    CargoResponseDto update(Long id, CargoRequestDto requestDto);

    /**
     * Deletes a cargo entity by its ID.
     *
     * @param id The ID of the cargo to delete.
     */
    void deleteById(Long id);

    /**
     * Retrieves a paginated and filtered list of cargo entities.
     *
     * @param pageable            Pagination information.
     * @param searchParametersDto Parameters for filtering cargo entities.
     * @return The paginated list of cargo DTOs.
     */
    CargoListResponseDto getPaginatedFilteredList(Pageable pageable,
                                                  CargoSearchRequestDto searchParametersDto);

    /**
     * Retrieves a filtered list of cargo entities.
     *
     * @param searchParametersDto Parameters for filtering cargo entities.
     * @return The filtered list of cargo DTOs.
     */
    List<CargoResponseDto> getFilteredList(CargoSearchRequestDto searchParametersDto);
}
