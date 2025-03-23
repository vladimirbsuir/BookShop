package com.example.bookshop.controller;

import com.example.bookshop.service.LogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** Class to handle logs requests. */
@RestController
@RequestMapping("/logs")
@Tag(name = "Log requests", description = "Operations with .log file")
public class LogController {

    private final LogService logService;

    /** Constructor of the class. */
    public LogController(LogService logService) {
        this.logService = logService;
    }

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
        return logService.downloadLogs(date);
    }
}