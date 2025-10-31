package com.realtime.model;

import java.time.LocalDateTime;

public class Metric {
    private final String name;
    private final LocalDateTime timestamp;
    private final double value;

    public Metric(String name, double value) {
        this.name = name;
        this.timestamp = LocalDateTime.now();
        this.value = value;
    }

    public String getName() { return name; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public double getValue() { return value; }

    public String toSimpleString() {
        return String.format("{\"name\":\"%s\", \"time\":\"%s\", \"value\":%.4f}",
                name, timestamp.toString(), value);
    }
}