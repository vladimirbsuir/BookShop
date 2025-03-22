package com.example.bookshop.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/** Class to handle IO exceptions. */
@ControllerAdvice
public class IoExceptionHandler implements IExceptionHandler {

    /** Function to handle constraint violation types. */
    @ExceptionHandler(CreateTempFileException.class)
    public ResponseEntity<Object> handleConstraintViolation(CreateTempFileException e) {
        return createException(HttpStatus.BAD_REQUEST, "Invalid parameter value", e.getMessage());
    }
}
