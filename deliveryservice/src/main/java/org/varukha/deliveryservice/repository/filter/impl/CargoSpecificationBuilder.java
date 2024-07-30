package org.varukha.deliveryservice.repository.filter.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.varukha.deliveryservice.dto.cargo.CargoSearchRequestDto;
import org.varukha.deliveryservice.model.Cargo;
import org.varukha.deliveryservice.repository.filter.SpecificationBuilder;
import org.varukha.deliveryservice.repository.filter.SpecificationProviderManager;

/**
 * Builds specifications based on search parameters for cargo entities.
 * This component provides methods to generate specifications for cargo entities
 * based on various search parameters such as weight and status.
 */
@RequiredArgsConstructor
@Component
public class CargoSpecificationBuilder implements SpecificationBuilder<Cargo> {
    public static final String CARGO_STATUS = "status";
    public static final String VEHICLE_TYPE_KEY = "type";

    private final SpecificationProviderManager<Cargo> specificationProviderManager;

    /**
     * Builds a specification based on the provided search parameters.
     *
     * @param searchParametersDto The search parameters for cargo entities
     * @return The generated specification
     */
    @Override
    public Specification<Cargo> build(CargoSearchRequestDto searchParametersDto) {
        Specification<Cargo> spec = Specification.where(null);
        spec = getCargoSpecification(searchParametersDto.status(), spec, CARGO_STATUS);
        spec = getCargoSpecification(searchParametersDto.type(), spec, VEHICLE_TYPE_KEY);
        return spec;
    }

    /**
     * Retrieves a cargo specification based on the provided parameters.
     *
     * @param searchParametersDto The search parameters for cargo entities
     * @param spec The existing specification to be updated
     * @param key The key representing the type of specification
     * @return The updated specification
     */
    private Specification<Cargo> getCargoSpecification(String[] searchParametersDto,
                                                     Specification<Cargo> spec,
                                                     String key) {
        if (searchParametersDto != null && searchParametersDto.length > 0) {
            spec = spec.and(specificationProviderManager.getSpecificationProvider(key)
                    .getSpecification(searchParametersDto));
        }
        return spec;
    }
}
