package com.example.bookshop.exception;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/** Class to handle request exceptions. */
@ControllerAdvice
public class RequestExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String STATUS = "status";
    private static final String TIME = "time";
    private static final String MESSAGE = "message";
    private static final String ERROR = "error";

    /** Class to handle resource not found exception. */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFound(ResourceNotFoundException e, WebRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put(TIME, new Date());
        body.put(STATUS, HttpStatus.NOT_FOUND.value());
        body.put(ERROR, e.getMessage());
        body.put(MESSAGE, request.getDescription(false));

        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    /** Function to handle general exceptions. */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGeneralException(Exception e, WebRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put(TIME, new Date());
        body.put(STATUS, HttpStatus.INTERNAL_SERVER_ERROR.value());
        body.put(ERROR, e.getMessage());
        body.put(MESSAGE, request.getDescription(false));
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}