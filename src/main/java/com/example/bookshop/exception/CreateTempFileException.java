package com.example.bookshop.exception;

import org.springframework.http.HttpStatus;

/** Class to handle create temp file exception. */
public class CreateTempFileException extends BasicException {

    /** Constructor to create exception. */
    public CreateTempFileException(HttpStatus status, String message) {
        super(status, message);
    }
}
