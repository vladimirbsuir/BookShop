package com.example.bookshop.exception;

import org.springframework.http.HttpStatus;

/** Interface for other exceptions to implement. */
public class BasicException extends RuntimeException {
    final HttpStatus status;
    final String message;

    /** Constructor of the class. */
    public BasicException(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
