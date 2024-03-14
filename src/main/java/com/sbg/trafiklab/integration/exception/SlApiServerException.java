package com.sbg.trafiklab.integration.exception;

public class SlApiServerException extends RuntimeException {

    public SlApiServerException(String message) {
        super(message);
    }

    public SlApiServerException(String message, Throwable cause) {
        super(message, cause);
    }

}
