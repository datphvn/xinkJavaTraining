package com.platform.model;

import java.time.LocalDateTime;

public class Metric {
    public final String name;
    private final LocalDateTime timestamp;
    private final double value;
    private final String type; // e.g., COUNT, AVG, LATENCY

    public Metric(String name, double value, String type) {
        this.name = name;
        this.timestamp = LocalDateTime.now();
        this.value = value;
        this.type = type;
    }

    @Override
    public String toString() {
        return String.format("METRIC [%s] %s: %.2f", type, name, value);
    }
}