package com.etl.model;

import java.util.Optional;

// Lưu trữ kết quả của quá trình ETL
public class ETLResult {
    private final boolean success;
    private final long processedCount;
    private final long errorCount;
    private final long durationMs;
    private final Optional<Exception> failureCause;

    private ETLResult(boolean success, long processedCount, long errorCount, long durationMs, Exception failureCause) {
        this.success = success;
        this.processedCount = processedCount;
        this.errorCount = errorCount;
        this.durationMs = durationMs;
        this.failureCause = Optional.ofNullable(failureCause);
    }

    // Factory method cho thành công
    public static ETLResult success(long processedCount, long errorCount, long durationMs) {
        return new ETLResult(true, processedCount, errorCount, durationMs, null);
    }

    // Factory method cho thất bại (pipeline bị dừng)
    public static ETLResult failure(Exception cause, long processedCount, long errorCount) {
        return new ETLResult(false, processedCount, errorCount, 0, cause);
    }

    // Getters
    public boolean isSuccess() { return success; }
    public long getProcessedCount() { return processedCount; }
    public long getErrorCount() { return errorCount; }
    public long getDurationMs() { return durationMs; }
    public Optional<Exception> getFailureCause() { return failureCause; }

    @Override
    public String toString() {
        return String.format("ETLResult [Success: %b, Processed: %d, Errors: %d, Duration: %d ms, Cause: %s]",
                success, processedCount, errorCount, durationMs, failureCause.map(Exception::getMessage).orElse("None"));
    }
}