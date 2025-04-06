package com.example.bookshop.service;

import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Service;

/** Class to handle logic for visits count. */
@Service
public class CounterService {
    private final AtomicLong counter = new AtomicLong(0);

    /** Function to increment amount of visits. */
    public void increment() {
        counter.incrementAndGet();
    }

    /** Function to get total amount of visits.
     *
     * @return total amount of visits.
     */
    public long getTotalVisits() {
        return counter.get();
    }
}