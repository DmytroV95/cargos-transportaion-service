package org.varukha.deliveryservice.exception;

/**
 * Exception thrown when there is an error saving an entity.
 */
public class EntitySaveException extends RuntimeException {
    public EntitySaveException(String message, Throwable cause) {
        super(message, cause);
    }
}
