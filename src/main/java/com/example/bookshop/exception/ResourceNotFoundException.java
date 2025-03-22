package com.example.bookshop.exception;

import org.springframework.http.HttpStatus;

/** Custom exception for entity not found exception. */
public class ResourceNotFoundException extends BasicException {

    /** Constructor of the class. */
    public ResourceNotFoundException(HttpStatus status, String message) {
        super(status, message);
    }
}
