package com.realtime.model;

import java.time.LocalDateTime;

public class HistoricalRecord {
    private final LocalDateTime timestamp;
    private final String dataType;
    private final String dataString; // Lưu dưới dạng String đơn giản

    public HistoricalRecord(LocalDateTime timestamp, String dataType, String dataString) {
        this.timestamp = timestamp;
        this.dataType = dataType;
        this.dataString = dataString;
    }

    public LocalDateTime getTimestamp() { return timestamp; }
    public String getDataType() { return dataType; }
    public String getDataString() { return dataString; }
}