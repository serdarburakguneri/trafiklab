package com.sbg.trafiklab.exception;

public class EntityCreationFailureException extends RuntimeException {

    public EntityCreationFailureException(String message) {
        super(message);
    }

    public EntityCreationFailureException(String message, Throwable cause) {
        super(message, cause);
    }

    public EntityCreationFailureException(Throwable cause) {
        super(cause);
    }

}
