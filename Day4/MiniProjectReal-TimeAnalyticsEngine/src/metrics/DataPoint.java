package metrics;

public class DataPoint {
    public final long timestamp;
    public final double value;

    public DataPoint(long timestamp, double value) {
        this.timestamp = timestamp;
        this.value = value;
    }
}
