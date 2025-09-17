package analytics;

public class Event {
    private final String type;
    private final long timestamp;
    private final double value;

    public Event(String type, long timestamp, double value) {
        this.type = type;
        this.timestamp = timestamp;
        this.value = value;
    }

    public String getType() { return type; }
    public long getTimestamp() { return timestamp; }
    public double getValue() { return value; }
}
