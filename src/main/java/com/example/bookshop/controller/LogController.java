package com.example.bookshop.controller;

import com.example.bookshop.model.LogObj;
import com.example.bookshop.service.LogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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

    /** Function to create log file.
     *
     * @param date date of the logs
     * @return id of the log creation task
     */
    @Operation(summary = "Create log file", description = "Starts log file creation")
    @PostMapping("/generate")
    public ResponseEntity<Long> createLogs(
            @Parameter(description = "Date for logs", example = "19-03-2025")
            @RequestParam String date) {
        Long taskId = logService.startLogCreation(date);
        return ResponseEntity.accepted().body(taskId);
    }

    /** Function to check status of log creation task.
     *
     * @param taskId id of the log creation task
     * @return status of the task and error message in case of exception
     */
    @Operation(summary = "Check task status", description = "Returns current task status")
    @GetMapping("/status/{taskId}")
    public ResponseEntity<Map<String, String>> checkStatus(
            @Parameter(description = "Task ID")
            @PathVariable Long taskId) {
        LogObj task = logService.getStatus(taskId);
        Map<String, String> response = new HashMap<>();
        response.put("status", task.getStatus());
        if (task.getErrorMessage() != null) {
            response.put("error", task.getErrorMessage());
        }
        return ResponseEntity.ok(response);
    }

    /** Function to download created file with specified logs.
     *
     * @param taskId id of the task
     * @return file with specified logs
     */
    @Operation(summary = "Download created logs", description = "Downloads generated log file by ID")
    @GetMapping("/download/{taskId}")
    public ResponseEntity<Resource> downloadCreatedLogs(
            @Parameter(description = "Task ID")
            @PathVariable Long taskId) throws IOException {
        return logService.downloadCreatedLogs(taskId);
    }
}