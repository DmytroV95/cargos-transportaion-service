package com.varukha.emailservice.exception;

public class EmailListenerException extends RuntimeException {
    public EmailListenerException(String message, Throwable cause) {
        super(message, cause);
    }
}
