package com.realtime.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Event {
    private final String id;
    private final LocalDateTime timestamp;
    private final String type; // e.g., "PAGE_VIEW", "TRANSACTION", "ERROR"
    private final Double value; // Numerical value for averaging/summing

    public Event(String type, Double value) {
        this.id = UUID.randomUUID().toString();
        this.timestamp = LocalDateTime.now();
        this.type = type;
        this.value = value;
    }

    public Event(LocalDateTime timestamp, String type, Double value) {
        this.id = UUID.randomUUID().toString();
        this.timestamp = timestamp;
        this.type = type;
        this.value = value;
    }

    public String getId() { return id; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public String getType() { return type; }
    public Double getValue() { return value; }

    // Sử dụng String.format thay vì thư viện JSON
    public String toSimpleString() {
        return String.format("{\"id\":\"%s\", \"time\":\"%s\", \"type\":\"%s\", \"value\":%.2f}",
                id, timestamp.toString(), type, value);
    }

    @Override
    public String toString() {
        return toSimpleString();
    }
}