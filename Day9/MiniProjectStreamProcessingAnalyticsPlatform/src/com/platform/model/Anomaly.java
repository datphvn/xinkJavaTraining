package com.platform.model;

import java.time.LocalDateTime;

public class Anomaly {
    private final LocalDateTime timestamp;
    private final String key;
    private final double currentValue;
    private final String reason;

    public Anomaly(LocalDateTime timestamp, String key, double currentValue, String reason) {
        this.timestamp = timestamp;
        this.key = key;
        this.currentValue = currentValue;
        this.reason = reason;
    }

    // --- Getters được thêm vào để giải quyết lỗi ---

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getKey() {
        return key;
    }

    public double getCurrentValue() {
        return currentValue;
    }

    public String getReason() {
        return reason;
    }

    // --- Kết thúc Getters ---

    @Override
    public String toString() {
        return String.format("ANOMALY [%s] at %s: %.2f (%s)", key, timestamp, currentValue, reason);
    }
}