package com.example.bookshop.exception;

import jakarta.validation.ConstraintViolationException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/** Class to handle argument exceptions. */
@ControllerAdvice
public class ArgumentExceptionHandler {

    private static final String STATUS = "status";
    private static final String TIME = "time";
    private static final String MESSAGE = "message";
    private static final String ERROR = "error";

    /** Function to handle incorrect method arguments exceptions. */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        Map<String, Object> body = new HashMap<>();
        body.put(TIME, new Date());
        body.put(STATUS, HttpStatus.UNPROCESSABLE_ENTITY.value());
        body.put(ERROR, "Validation error");
        body.put(MESSAGE, e.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toList());
        return new ResponseEntity<>(body, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    /** Function to handle incorrect argument types. */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> handleTypeMismatch(MethodArgumentTypeMismatchException e) {
        Map<String, Object> body = new HashMap<>();
        body.put(TIME, new Date());
        body.put(STATUS, HttpStatus.BAD_REQUEST.value());
        body.put(ERROR, "Invalid parameter type");
        body.put(MESSAGE, "Invalid value for parameter: " + e.getName() + ". Expected type: " + e.getRequiredType().getSimpleName());
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    /** Function to handle constraint violation types. */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException e) {
        Map<String, Object> body = new HashMap<>();
        body.put(TIME, new Date());
        body.put(STATUS, HttpStatus.BAD_REQUEST.value());
        body.put(ERROR, "Invalid parameter value");
        body.put(MESSAGE, e.getMessage());
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
}
