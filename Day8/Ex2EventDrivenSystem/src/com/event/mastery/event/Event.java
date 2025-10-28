package com.event.mastery.event;

import java.time.LocalDateTime;
import java.util.UUID;

// Lớp cơ sở cho tất cả các sự kiện
public abstract class Event {
    private final UUID id;
    private final LocalDateTime timestamp;
    private final String source;

    protected Event(String source) {
        this.id = UUID.randomUUID();
        this.timestamp = LocalDateTime.now();
        this.source = source;
    }

    public UUID getId() { return id; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public String getSource() { return source; }

    // Phương thức utility để lấy loại sự kiện (dùng cho routing)
    public String getType() {
        return this.getClass().getSimpleName();
    }

    @Override
    public String toString() {
        return String.format("%s [ID: %s, Source: %s, Time: %s]", getType(), id.toString().substring(0, 8), source, timestamp);
    }
}
