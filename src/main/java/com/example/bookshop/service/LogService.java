package com.example.bookshop.service;

import com.example.bookshop.exception.ResourceNotFoundException;
import com.example.bookshop.model.LogObj;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

/** Class to hold logic for operations with logs. */
@Service
public class LogService {
    private final AsyncLogService asyncLogService;
    private final CacheManager cacheManager;
    private final AtomicLong idCounter = new AtomicLong(1);

    /** Constructor of the class. */
    public LogService(AsyncLogService asyncLogService, CacheManager cacheManager) {
        this.asyncLogService = asyncLogService;
        this.cacheManager = cacheManager;
    }

    /** Function to start creating log file.
     *
     * @param date date of the logs
     * @return id of the task
     */
    public Long startLogCreation(String date) {
        Long id = idCounter.getAndIncrement();
        LogObj task = new LogObj(id, "IN_PROGRESS");
        Cache logsCache = cacheManager.getCache("logTasks");
        if (logsCache != null) {
            logsCache.put(id, task);
        }
        asyncLogService.createLogs(id, date, logsCache);
        return id;
    }

    /** Function to get status of creating log file.
     *
     * @param taskId id of the task
     * @return object of LogObj class
     */
    public LogObj getStatus(Long taskId) {
        Cache logsCache = cacheManager.getCache("logTasks");
        if (logsCache != null) {
            return logsCache.get(taskId, LogObj.class);
        } else {
            return null;
        }
    }

    /** Function to download file with specified logs.
     *
     * @param taskId id of the task
     * @return file with specified logs
     * @throws IOException if unable to write data from main log file
     */
    public ResponseEntity<Resource> downloadCreatedLogs(Long taskId) throws IOException {
        LogObj task = getStatus(taskId);
        if (task == null) {
            throw new ResourceNotFoundException(HttpStatus.NOT_FOUND, "Task not found");
        }
        if (!"COMPLETED".equals(task.getStatus())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Logs not ready");
        }

        Path path = Paths.get(task.getFilePath());
        Resource resource = new UrlResource(path.toUri());

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}