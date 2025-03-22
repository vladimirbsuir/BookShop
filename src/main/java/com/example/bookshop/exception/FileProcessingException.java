package com.example.bookshop.exception;

import org.springframework.http.HttpStatus;

/** Class to handle file processing exceptions. */
public class FileProcessingException extends BasicException {

    /** Constructor of the class. */
    public FileProcessingException(HttpStatus status, String message) {
        super(status, message);
    }
}
