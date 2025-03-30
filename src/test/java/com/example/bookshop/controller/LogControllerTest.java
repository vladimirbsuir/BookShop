package com.example.bookshop.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import com.example.bookshop.exception.FileProcessingException;
import com.example.bookshop.exception.InvalidValueFormatException;
import com.example.bookshop.service.LogService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

@ExtendWith(MockitoExtension.class)
class LogControllerTest {

    @Mock
    private LogService logService;

    @InjectMocks
    private LogController logController;

    @Test
    void downloadLogs_ReturnServiceResponse() {
        String date = "01-01-2024";
        ResponseEntity<Resource> expectedResponse = mock(ResponseEntity.class);
        when(logService.downloadLogs(date)).thenReturn(expectedResponse);

        ResponseEntity<Resource> actualResponse = logController.downloadLogs(date);

        assertSame(expectedResponse, actualResponse);
        verify(logService).downloadLogs(date);
    }

    @Test
    void downloadLogs_HandleInvalidDateFormat() {
        String invalidDate = "2024-01-01";
        when(logService.downloadLogs(invalidDate))
                .thenThrow(new InvalidValueFormatException(HttpStatus.BAD_REQUEST,
                        "Invalid date format. Required dd-mm-yyyy"));

        InvalidValueFormatException exception = assertThrows(
                InvalidValueFormatException.class,
                () -> logController.downloadLogs(invalidDate)
        );

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Invalid date format. Required dd-mm-yyyy", exception.getMessage());
    }

    @Test
    void downloadLogs_HandleNotFound() {
        String date = "01-01-2024";
        when(logService.downloadLogs(date))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Logs not found"));

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> logController.downloadLogs(date)
        );

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Logs not found", exception.getReason());
    }

    @Test
    void downloadLogs_HandleInternalErrors() {
        String date = "01-01-2024";
        when(logService.downloadLogs(date))
                .thenThrow(new FileProcessingException(HttpStatus.INTERNAL_SERVER_ERROR,
                        "File processing error"));

        FileProcessingException exception = assertThrows(
                FileProcessingException.class,
                () -> logController.downloadLogs(date)
        );

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatus());
        assertEquals("File processing error", exception.getMessage());
    }

    @Test
    void downloadLogs_HandleAllServiceExceptions() {
        String date = "01-01-2024";
        when(logService.downloadLogs(date))
                .thenThrow(new RuntimeException("Unexpected error"));

        assertThrows(RuntimeException.class,
                () -> logController.downloadLogs(date));
    }
}
