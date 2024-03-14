package com.sbg.trafiklab.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ApplicationExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationExceptionHandler.class);

    //in case we don't handle an exception properly, we can use something like this to handle it
    //there should be a proper message, an error code and an info to be linked to the logs

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> handleException(Exception ex) {

        var errorMessage = "We are sorry.";
        logger.error("An unexpected error occurred.", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorMessage(errorMessage));
    }

    record ErrorMessage(String message) {

    }
}

