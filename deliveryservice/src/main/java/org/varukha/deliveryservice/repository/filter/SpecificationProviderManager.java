package org.varukha.deliveryservice.repository.filter;

/**
 * Interface for managing specification providers based on keys.
 * This interface defines a method for retrieving a specification provider
 * based on a provided key.
 *
 * @param <T> The type of entity for which specification providers are managed
 */
public interface SpecificationProviderManager<T> {
    /**
     * Retrieves the specification provider based on the provided key.
     *
     * @param key The key associated with the specification provider
     * @return The specification provider for the given key
     */
    SpecificationProvider<T> getSpecificationProvider(String key);
}
