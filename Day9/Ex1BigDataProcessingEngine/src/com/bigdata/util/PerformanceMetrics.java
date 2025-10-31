package com.bigdata.util;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

// Utility for Performance Metrics
public class PerformanceMetrics {
    private Instant startTime;
    private Instant endTime;
    private final List<String> errorLogs = new ArrayList<>();
    private long totalRecordsProcessed = 0;
    private long totalErrors = 0;

    public void start() { startTime = Instant.now(); }
    public void stop() { endTime = Instant.now(); }

    public void logError(Exception e, String context) {
        totalErrors++;
        errorLogs.add(String.format("[%s] Error: %s", context, e.getMessage()));
    }

    public void recordProcessed(long count) { totalRecordsProcessed += count; }

    public Duration getDuration() {
        return (startTime != null && endTime != null) ? Duration.between(startTime, endTime) : Duration.ZERO;
    }

    public void printMetrics() {
        System.out.println("--- Performance Metrics ---");
        System.out.printf("Total Duration: %s%n", getDuration());
        System.out.printf("Records Processed: %d%n", totalRecordsProcessed);
        System.out.printf("Total Errors: %d%n", totalErrors);
        errorLogs.forEach(System.err::println);
        System.out.println("---------------------------");
    }
}
