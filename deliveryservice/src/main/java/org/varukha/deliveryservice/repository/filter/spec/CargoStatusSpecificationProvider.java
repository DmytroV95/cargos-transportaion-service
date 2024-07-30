package org.varukha.deliveryservice.repository.filter.spec;

import static org.varukha.deliveryservice.repository.filter.impl.CargoSpecificationBuilder.CARGO_STATUS;

import java.util.Arrays;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.varukha.deliveryservice.model.Cargo;
import org.varukha.deliveryservice.repository.filter.SpecificationProvider;

/**
 * Provides specifications for filtering cargo entities based on delivery status.
 */
@Component
public class CargoStatusSpecificationProvider implements SpecificationProvider<Cargo> {
    /**
     * Retrieves the key associated with this specification provider.
     *
     * @return The key of the specification provider
     */
    @Override
    public String getKey() {
        return CARGO_STATUS;
    }

    /**
     * Generates a specification to filter cargo entities based on delivery status.
     *
     * @param params The delivery statuses to filter by
     * @return The generated specification
     */
    public Specification<Cargo> getSpecification(String[] params) {
        return (root, query, criteriaBuilder) -> root.get(CARGO_STATUS)
                .in(Arrays.stream(params).toArray());
    }
}
