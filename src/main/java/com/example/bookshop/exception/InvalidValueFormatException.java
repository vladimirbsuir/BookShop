package com.example.bookshop.exception;

import org.springframework.http.HttpStatus;

/** Class to handle invalid value format exception. */
public class InvalidValueFormatException extends BasicException {

    /** Constructor of the class. */
    public InvalidValueFormatException(HttpStatus status, String message) {
        super(status, message);
    }
}
