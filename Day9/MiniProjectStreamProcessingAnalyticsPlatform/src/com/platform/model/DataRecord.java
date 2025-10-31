package com.platform.model;

import java.time.LocalDateTime;
import java.util.Map;

// Đối tượng dữ liệu trung tâm
public class DataRecord {
    private final LocalDateTime timestamp;
    private final String sourceId;
    private final String key;
    private final double value;
    private final Map<String, String> properties; // Hỗ trợ Schema Evolution

    public DataRecord(String sourceId, String key, double value, Map<String, String> properties) {
        this.timestamp = LocalDateTime.now();
        this.sourceId = sourceId;
        this.key = key;
        this.value = value;
        this.properties = properties;
    }

    // Getters
    public LocalDateTime getTimestamp() { return timestamp; }
    public String getSourceId() { return sourceId; }
    public String getKey() { return key; }
    public double getValue() { return value; }
    public Map<String, String> getProperties() { return properties; }

    @Override
    public String toString() {
        return String.format("{time: %s, key: %s, value: %.2f, props: %s}", timestamp, key, value, properties);
    }
}