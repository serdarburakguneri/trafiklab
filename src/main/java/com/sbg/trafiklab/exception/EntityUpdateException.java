package com.sbg.trafiklab.exception;

public class EntityUpdateException extends RuntimeException{

    public EntityUpdateException(String message) {
        super(message);
    }

    public EntityUpdateException(String message, Throwable cause) {
        super(message, cause);
    }

    public EntityUpdateException(Throwable cause) {
        super(cause);
    }

}
