package org.varukha.deliveryservice.repository.filter;

import org.springframework.data.jpa.domain.Specification;
import org.varukha.deliveryservice.model.Cargo;

/**
 * Interface for providing specifications based on specific parameters.
 * This interface defines methods for retrieving the key associated with
 * the provider and for generating specifications based on provided parameters.
 *
 * @param <T> The type of entity for which specifications are provided
 */
public interface SpecificationProvider<T> {
    /**
     * Retrieves the key associated with this specification provider.
     *
     * @return The key of the specification provider
     */
    String getKey();

    /**
     * Generates a specification based on the provided parameters.
     *
     * @param params The parameters used to generate the specification
     * @return The generated specification
     */
    Specification<Cargo> getSpecification(String[] params);
}
