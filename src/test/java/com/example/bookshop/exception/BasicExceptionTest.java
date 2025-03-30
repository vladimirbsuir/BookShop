package com.example.bookshop.exception;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class BasicExceptionTest {

    @Test
    void storeStatusAndMessage() {
        BasicException ex = new BasicException(HttpStatus.BAD_REQUEST, "Test message");

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
        assertEquals("Test message", ex.getMessage());
    }
}

class CreateTempFileExceptionTest {
    @Test
    void inheritFromBasicException() {
        CreateTempFileException ex = new CreateTempFileException(
                HttpStatus.BAD_REQUEST,
                "Temp file error"
        );

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
        assertEquals("Temp file error", ex.getMessage());
    }
}

class InvalidRequestFormatExceptionTest {
    @Test
    void containCorrectDetails() {
        InvalidRequestFormatException ex = new InvalidRequestFormatException(
                HttpStatus.BAD_REQUEST,
                "Invalid format"
        );

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
        assertEquals("Invalid format", ex.getMessage());
    }
}
