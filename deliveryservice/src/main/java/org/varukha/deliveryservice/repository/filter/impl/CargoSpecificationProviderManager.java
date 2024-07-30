package org.varukha.deliveryservice.repository.filter.impl;

import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.varukha.deliveryservice.model.Cargo;
import org.varukha.deliveryservice.repository.filter.SpecificationProvider;
import org.varukha.deliveryservice.repository.filter.SpecificationProviderManager;

/**
 * Manages cargo specification providers.
 * This component is responsible for managing and providing specification providers
 * for cargo entities based on specific criteria.
 */
@RequiredArgsConstructor
@Component
public class CargoSpecificationProviderManager implements SpecificationProviderManager<Cargo> {
    private final List<SpecificationProvider<Cargo>> cargoSpecificationProviders;

    /**
     * Retrieves the specification provider associated with the given key.
     *
     * @param key The key associated with the specification provider
     * @return The specification provider for the given key
     * @throws NoSuchElementException If no specification provider is found for the given key
     */
    @Override
    public SpecificationProvider<Cargo> getSpecificationProvider(String key) {
        return cargoSpecificationProviders
                .stream()
                .filter(p -> p.getKey().equals(key))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException(
                        "Can't find correct specification provider for key: " + key));
    }
}
