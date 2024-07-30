package org.varukha.deliveryservice.config;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.NullValueCheckStrategy;

/**
 * Configuration class for MapStruct mappers.
 * Specifies component model as "spring", injection strategy as "constructor",
 * null value check strategy as "always", and the implementation package for
 * generated mapper implementations.
 */
@org.mapstruct.MapperConfig(
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        implementationPackage = "<PACKAGE_NAME>.impl"
)
public class MapperConfig {
}
