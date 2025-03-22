package com.example.bookshop.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

/** Class to handle request exceptions. */
@ControllerAdvice
public class RequestExceptionHandler implements IExceptionHandler {

    /** Class to handle resource not found exception. */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFound(ResourceNotFoundException e, WebRequest request) {
        return createException(e, request.getDescription(false));
    }

    /** Function to handle general exceptions. */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGeneralException(Exception e, WebRequest request) {
        return createException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), request.getDescription(false));
    }

    /** Function to handle http message not readable exception. */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException e) {
        return createException(new InvalidRequestFormatException(
                HttpStatus.BAD_REQUEST, "Invalid JSON view"), e.getMessage());
    }
}