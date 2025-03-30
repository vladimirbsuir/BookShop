package com.example.bookshop.exception;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.Map;

class IoExceptionHandlerTest {

    private final IoExceptionHandler handler = new IoExceptionHandler();

    @Test
    void handleCreateTempFileException_ReturnBadRequest() {
        CreateTempFileException ex = new CreateTempFileException(
                HttpStatus.BAD_REQUEST,
                "File creation failed"
        );

        ResponseEntity<Object> response = handler.handleConstraintViolation(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertNotNull(body);
        assertEquals("Invalid parameter value", body.get("error"));
        assertEquals("File creation failed", body.get("message"));
    }
}
