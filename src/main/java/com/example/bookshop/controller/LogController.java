package com.example.bookshop.controller;

import com.example.bookshop.exception.CreateTempFileException;
import com.example.bookshop.exception.FileProcessingException;
import com.example.bookshop.exception.InvalidValueFormatException;
import com.example.bookshop.exception.ResourceNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang3.SystemUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/** Class to handle logs requests. */
@RestController
@RequestMapping("/logs")
@Tag(name = "Log requests", description = "Operations with .log file")
public class LogController {

    private static final String LOG_FILE_PATH = "app.log";

    /** Function to return .log file with logs for specified date. */
    @Operation(summary = "Get .log file", description = "Returns .log file with logs from specified date",
            responses = {
                @ApiResponse(responseCode = "200", description =
                            "Log was downloaded"),
                @ApiResponse(responseCode = "400", description =
                            "Invalid date format",
                            content = @Content(schema = @Schema(example =
                                    "{ \"error\": \"Invalid date format. Required dd-mm-yyyy\" }"))),
                @ApiResponse(responseCode = "404", description =
                            "Logs not found",
                            content = @Content(schema = @Schema(example =
                                    "{ \"error\": \"Logs not found\" }"))),
                @ApiResponse(responseCode = "500", description =
                            "Internal server error",
                            content = @Content(schema = @Schema(example =
                                    "{ \"error\": \"Internal server error\" }")))})
    @GetMapping("/get-logs")
    public ResponseEntity<Resource> downloadLogs(@Parameter(description = "date to get logs for",
            example = "19-03-2025") @RequestParam String date) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDate logDate = LocalDate.parse(date, formatter);

            Path path = Paths.get(LOG_FILE_PATH);
            if (!Files.exists(path)) {
                throw new ResourceNotFoundException(HttpStatus.INTERNAL_SERVER_ERROR,
                        "File doesn't exist: " + LOG_FILE_PATH);
            }

            String formattedDate = logDate.format(formatter);
            List<String> logLines = Files.readAllLines(path);
            List<String> currentLogs = logLines.stream()
                    .filter(line -> line.startsWith(formattedDate))
                    .toList();

            if (currentLogs.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "There are no logs for specified date: " + date);
            }

            Path logFile;

            if (SystemUtils.IS_OS_UNIX) {
                FileAttribute<Set<PosixFilePermission>> attr =
                        PosixFilePermissions.asFileAttribute(PosixFilePermissions.fromString("rwx------"));
                logFile = Files.createTempFile("logs-" + logDate, ".log", attr);
            } else {
                path = Files.createTempFile("logs-" + logDate, ".log");
                File file = path.toFile();
                if (file.setReadable(true, true)
                        && file.setWritable(true, true)
                        && file.setExecutable(true, true)) {
                    logFile = path;
                } else {
                    throw new CreateTempFileException(HttpStatus.INTERNAL_SERVER_ERROR,
                            "Failed to create temp file");
                }
            }

            Files.write(logFile, currentLogs);

            Resource resource = new UrlResource(logFile.toUri());
            logFile.toFile().deleteOnExit();

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } catch (DateTimeParseException e) {
            throw new InvalidValueFormatException(HttpStatus.BAD_REQUEST,
                    "Invalid date format. Required dd-mm-yyyy");
        } catch (IOException e) {
            throw new FileProcessingException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error processing log file. " + e.getMessage());
        }
    }
}