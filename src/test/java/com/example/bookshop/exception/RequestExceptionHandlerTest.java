package com.example.bookshop.exception;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

class RequestExceptionHandlerTest {

    private final RequestExceptionHandler handler = new RequestExceptionHandler();

    @Test
    void handleResourceNotFound_ReturnNotFoundResponse() {
        ResourceNotFoundException ex = new ResourceNotFoundException(HttpStatus.NOT_FOUND, "Resource not found");
        WebRequest request = mock(WebRequest.class);

        ResponseEntity<Object> response = handler.handleResourceNotFound(ex, request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertNotNull(body);
    }

    @Test
    void handleGeneralException_ReturnInternalServerError() {
        Exception ex = new Exception("Unexpected error");
        WebRequest request = mock(WebRequest.class);

        ResponseEntity<Object> response = handler.handleGeneralException(ex, request);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertNotNull(body);
        assertEquals("Unexpected error", body.get("error"));
    }
}
