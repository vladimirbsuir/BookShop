package com.example.bookshop.exception;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/** Interface for exception handlers. */
@SuppressWarnings("checkstyle:AbbreviationAsWordInName")
public interface IExceptionHandler {

    /** Function to create exception with corresponding information.
     *
     * @param status status of the error
     * @param error description of the error
     * @param message details of the error
     * @return JSON form of the exception
     */
    default ResponseEntity<Object> createException(HttpStatus status,
                                                                String error, String message) {
        Map<String, Object> body = new HashMap<>();
        body.put("time", new Date());
        body.put("status", status.value());
        body.put("error", error);
        body.put("message", message);

        return new ResponseEntity<>(body, status);
    }

    /** Function to create exception with corresponding information.
     *
     * @param e exception to handle
     * @param error description of the error
     * @return JSON form of the exception
     */
    default ResponseEntity<Object> createException(BasicException e, String error) {
        Map<String, Object> body = new HashMap<>();
        body.put("time", new Date());
        body.put("status", e.getStatus().value());
        body.put("error", error);
        body.put("message", e.getMessage());

        return new ResponseEntity<>(body, e.getStatus());
    }
}
