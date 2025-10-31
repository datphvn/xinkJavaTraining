package com.bigdata.model;

import java.time.LocalDateTime;

public class DataRecord {
    private final String category;
    private final double value;
    private final LocalDateTime timestamp;

    public DataRecord(String category, double value, LocalDateTime timestamp) {
        this.category = category;
        this.value = value;
        this.timestamp = timestamp;
    }

    public String getCategory() { return category; }
    public double getValue() { return value; }
    public LocalDateTime getTimestamp() { return timestamp; }
}
