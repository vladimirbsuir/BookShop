package com.example.bookshop.exception;

import org.springframework.http.HttpStatus;

/** Class to handle invalid value type exception. */
public class InvalidValueTypeException extends BasicException {

    /** Constructor of the class. */
    public InvalidValueTypeException(HttpStatus status, String message) {
        super(status, message);
    }
}
