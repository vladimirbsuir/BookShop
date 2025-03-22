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
public class ArgumentExceptionHandler implements IExceptionHandler {

    /** Function to handle incorrect method arguments exceptions. */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        Map<String, Object> body = new HashMap<>();
        body.put("time", new Date());
        body.put("status", HttpStatus.UNPROCESSABLE_ENTITY.value());
        body.put("error", "Validation error");
        body.put("message", e.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toList());
        return new ResponseEntity<>(body, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    /** Function to handle incorrect argument types. */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> handleTypeMismatch(MethodArgumentTypeMismatchException e) {
        String message = "Invalid value for parameter: " + e.getName()
                + ". Expected type: " + e.getRequiredType().getSimpleName();
        return createException(new InvalidValueTypeException(HttpStatus.BAD_REQUEST,
                "Invalid parameter type"), message);
    }

    /** Function to handle constraint violation types. */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException e) {
        return createException(HttpStatus.BAD_REQUEST, "Invalid parameter value", e.getMessage());
    }

    /** Function to handle constraint violation types. */
    @ExceptionHandler(InvalidValueFormatException.class)
    public ResponseEntity<Object> handleInvalidValueFormat(InvalidValueFormatException e) {
        return createException(e, "Invalid value format");
    }
}