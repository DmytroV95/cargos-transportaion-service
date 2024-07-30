package org.varukha.deliveryservice.repository.filter.spec;

import static org.varukha.deliveryservice.repository.filter.impl.CargoSpecificationBuilder.VEHICLE_TYPE_KEY;

import jakarta.persistence.criteria.JoinType;
import java.util.Arrays;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.varukha.deliveryservice.model.Cargo;
import org.varukha.deliveryservice.repository.filter.SpecificationProvider;

/**
 * Provides specifications for filtering cargo entities based on vehicle type.
 */
@Component
public class VehicleTypeSpecificationProvider implements SpecificationProvider<Cargo> {
    private static final String ENTITY_TYPE_VEHICLE = "vehicle";

    /**
     * Retrieves the key associated with this specification provider.
     *
     * @return The key of the specification provider
     */
    @Override
    public String getKey() {
        return VEHICLE_TYPE_KEY;
    }

    /**
     * Generates a specification to filter cargo entities based on vehicle type.
     *
     * @param params The vehicle types to filter by
     * @return The generated specification
     */
    public Specification<Cargo> getSpecification(String[] params) {
        return (root, query, criteriaBuilder) -> root.join(ENTITY_TYPE_VEHICLE, JoinType.INNER)
                .get(VEHICLE_TYPE_KEY).in(Arrays.asList(params));
    }
}
