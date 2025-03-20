package com.example.bookshop.exception;

/** Custom exception for entity not found exception. */
public class ResourceNotFoundException extends RuntimeException {

    /** Constructor of the class. */
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
