package metrics;

import analytics.Event;
import storage.CircularBuffer;

public class MetricStore {
    private final CircularBuffer<DataPoint> dataPoints = new CircularBuffer<>(1000);
    private final SlidingWindowStats stats = new SlidingWindowStats();
    private final TopKTracker<String> topK = new TopKTracker<>();

    public void addEvent(Event event) {
        DataPoint dp = new DataPoint(event.getTimestamp(), event.getValue());
        dataPoints.add(dp);
        stats.update(dp);
        topK.increment(event.getType());
    }

    public void expireOldData() {
        long now = System.currentTimeMillis();
        while (!dataPoints.isEmpty() && (now - dataPoints.peek().timestamp > 60000)) {
            dataPoints.poll();
        }
    }

    public SlidingWindowStats getStats() { return stats; }
    public TopKTracker<String> getTopK() { return topK; }
}
