package com.example.bookshop.exception;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import java.util.*;

class ArgumentExceptionHandlerTest {

    private final ArgumentExceptionHandler handler = new ArgumentExceptionHandler();

    @Test
    void handleMethodArgumentNotValid_ReturnValidationErrors() {
        BindingResult bindingResult = mock(BindingResult.class);
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);

        List<FieldError> fieldErrors = List.of(
                new FieldError("object", "title", "Title is required"),
                new FieldError("object", "author", "Author is invalid")
        );

        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(fieldErrors);

        ResponseEntity<Object> response = handler.handleMethodArgumentNotValid(ex);

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertNotNull(body);
        assertEquals("Validation error", body.get("error"));
        assertEquals(2, ((List<?>) body.get("message")).size());
    }

    @Test
    void handleTypeMismatch_ReturnTypeMismatchError() {
        MethodArgumentTypeMismatchException ex = mock(MethodArgumentTypeMismatchException.class);
        when(ex.getName()).thenReturn("page");
        when(ex.getRequiredType()).thenReturn((Class) Integer.class);

        ResponseEntity<Object> response = handler.handleTypeMismatch(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertNotNull(body);
    }

    @Test
    void handleConstraintViolation_ReturnViolationMessages() {
        Set<ConstraintViolation<?>> violations = new HashSet<>();
        ConstraintViolation<?> violation = mock(ConstraintViolation.class);
        Path path = mock(Path.class);

        when(violation.getMessage()).thenReturn("Invalid value");
        when(violation.getPropertyPath()).thenReturn(path);
        when(path.toString()).thenReturn("fieldName");

        violations.add(violation);
        ConstraintViolationException ex = new ConstraintViolationException(violations);

        ResponseEntity<Object> response = handler.handleConstraintViolation(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertNotNull(body);
        assertEquals("Invalid parameter value", body.get("error"));
        assertEquals("fieldName: Invalid value", body.get("message"));
    }
}
