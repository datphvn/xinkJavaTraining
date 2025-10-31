package com.bigdata.model;

import java.time.LocalDateTime;

public class TimeSeriesData {
    private final LocalDateTime timestamp;
    private final double value;

    public TimeSeriesData(LocalDateTime timestamp, double value) {
        this.timestamp = timestamp;
        this.value = value;
    }
    public LocalDateTime getTimestamp() { return timestamp; }
    public double getValue() { return value; }
}
