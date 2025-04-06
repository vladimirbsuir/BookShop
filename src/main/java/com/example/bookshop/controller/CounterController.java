package com.example.bookshop.controller;

import com.example.bookshop.service.CounterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Class to control visits. */
@RestController
@RequestMapping("/visit")
@Tag(name = "Visit tracking", description = "Visit counter operations")
public class CounterController {

    private final CounterService counterService;

    /** Constructor of the class. */
    public CounterController(CounterService counterService) {
        this.counterService = counterService;
    }

    /** Function to increment counter of visits. */
    @Operation(summary = "Register visit", description = "Increments visit counter")
    @GetMapping
    public void regVisit() {
        counterService.increment();
    }

    /** Function to get amount of all visits.
     *
     * @return amount of total visits.
     */
    @Operation(summary = "Get visit count", description = "Returns visit amount")
    @GetMapping("/count")
    public ResponseEntity<Map<String, Object>> getStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalVisits", counterService.getTotalVisits());

        return ResponseEntity.ok(stats);
    }
}