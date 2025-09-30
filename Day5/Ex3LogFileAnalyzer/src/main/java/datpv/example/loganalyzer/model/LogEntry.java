package datpv.example.loganalyzer.model;

import java.time.OffsetDateTime;
import java.util.Map;

public class LogEntry {
    private final OffsetDateTime timestamp;
    private final String level;
    private final String message;
    private final Map<String, Object> attributes;

    public LogEntry(OffsetDateTime timestamp, String level, String message, Map<String, Object> attributes) {
        this.timestamp = timestamp;
        this.level = level;
        this.message = message;
        this.attributes = attributes;
    }

    public OffsetDateTime getTimestamp() { return timestamp; }
    public String getLevel() { return level; }
    public String getMessage() { return message; }
    public Map<String, Object> getAttributes() { return attributes; }

    @Override
    public String toString() {
        return "LogEntry{" + timestamp + " " + level + " " + message + " attrs=" + attributes + '}';
    }
}
