package org.varukha.deliveryservice.repository.filter;

import org.springframework.data.jpa.domain.Specification;
import org.varukha.deliveryservice.dto.cargo.CargoSearchRequestDto;

/**
 * Interface for building specifications based on search parameters.
 * This interface defines a method for building specifications based on
 * search parameters for entities of type T.
 *
 * @param <T> The type of entity for which specifications are built
 */
public interface SpecificationBuilder<T> {
    /**
     * Builds a specification based on the provided search parameters.
     *
     * @param searchParametersDto The search parameters for entities of type T
     * @return The generated specification
     */
    Specification<T> build(CargoSearchRequestDto searchParametersDto);
}
