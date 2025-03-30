package com.example.bookshop.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import com.example.bookshop.exception.InvalidValueFormatException;
import com.example.bookshop.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class LogServiceTest {

    @InjectMocks
    private LogService logService;

    @TempDir
    Path tempDir;

    @Test
    void downloadLogs_ReturnLogsForDate() throws IOException {
        String date = "01-01-2024";
        Path logFile = tempDir.resolve("app.log");
        Files.write(logFile, List.of(
                "01-01-2024 10:00:00 - INFO: Test log",
                "02-01-2024 11:00:00 - INFO: Another log"
        ));

        try (MockedStatic<Paths> pathsMock = Mockito.mockStatic(Paths.class)) {
            pathsMock.when(() -> Paths.get("app.log")).thenReturn(logFile);

            ResponseEntity<Resource> response = logService.downloadLogs(date);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
        }
    }

    @Test
    void downloadLogs_ThrowWhenNoLogsForDate() throws IOException {
        String date = "01-01-2024";
        Path logFile = tempDir.resolve("app.log");
        Files.write(logFile, List.of("02-01-2024 11:00:00 - INFO: Log"));

        try (MockedStatic<Paths> pathsMock = Mockito.mockStatic(Paths.class)) {
            pathsMock.when(() -> Paths.get("app.log")).thenReturn(logFile);

            ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                    () -> logService.downloadLogs(date));

            assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        }
    }

    @Test
    void downloadLogs_ThrowForInvalidDateFormat() {
        InvalidValueFormatException exception = assertThrows(InvalidValueFormatException.class,
                () -> logService.downloadLogs("2024-01-01"));

        assertEquals("Invalid date format. Required dd-mm-yyyy", exception.getMessage());
    }

    @Test
    void downloadLogs_ShouldHandleTempFileCreationError() throws IOException {
        String date = "01-01-2024";
        Path logFile = tempDir.resolve("app.log");
        Files.write(logFile, List.of("01-01-2024 10:00:00 - INFO: Test log"));

        try (MockedStatic<Paths> pathsMock = Mockito.mockStatic(Paths.class);
             MockedStatic<Files> filesMock = Mockito.mockStatic(Files.class)) {

            pathsMock.when(() -> Paths.get("app.log")).thenReturn(logFile);
            filesMock.when(() -> Files.createTempFile(anyString(), anyString(), any()))
                    .thenThrow(new IOException("Create error"));

            assertThrows(ResourceNotFoundException.class,
                    () -> logService.downloadLogs(date));
        }
    }
}