package com.sbg.trafiklab.integration.exception;

public class SLApiClientException extends RuntimeException {

    public SLApiClientException(String message) {
        super(message);
    }

    public SLApiClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
