package com.example.bookshop.service;

import com.example.bookshop.exception.ResourceNotFoundException;
import com.example.bookshop.model.LogObj;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.springframework.cache.Cache;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/** Class to perform asynchronous actions with logs. */
@Service
public class AsyncLogService {
    private static final String LOG_FILE_PATH = "app.log";

    /** Function to create log file with specified logs.
     *
     * @param taskId id of the task
     * @param date date of the logs
     * @param logsCache cache where log tasks stored
     */
    @Async("taskExecutor")
    public void createLogs(Long taskId, String date, Cache logsCache) {
        try {
            Thread.sleep(5000);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDate logDate = LocalDate.parse(date, formatter);

            Path path = Paths.get(LOG_FILE_PATH);
            List<String> logLines = Files.readAllLines(path);
            String formattedDate = logDate.format(formatter);
            List<String> currentLogs = logLines.stream()
                    .filter(line -> line.startsWith(formattedDate))
                    .toList();

            if (currentLogs.isEmpty()) {
                throw new ResourceNotFoundException(HttpStatus.NOT_FOUND, "No logs for date: " + date);
            }

            Path logFile = Files.createTempFile("logs-" + formattedDate, ".log");
            Files.write(logFile, currentLogs);
            logFile.toFile().deleteOnExit();

            LogObj task = new LogObj(taskId, "COMPLETED");
            task.setFilePath(logFile.toString());
            logsCache.put(taskId, task);

        } catch (IOException e) {
            LogObj task = new LogObj(taskId, "FAILED");
            task.setErrorMessage(e.getMessage());
            logsCache.put(taskId, task);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
