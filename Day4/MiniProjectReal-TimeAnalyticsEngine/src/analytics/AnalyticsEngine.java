package analytics;

import metrics.MetricStore;
import detection.AnomalyDetector;

import java.util.concurrent.*;
import java.util.*;

public class AnalyticsEngine {
    private final ConcurrentHashMap<String, MetricStore> metrics = new ConcurrentHashMap<>();
    private final PriorityQueue<Event> eventQueue = new PriorityQueue<>(Comparator.comparingLong(Event::getTimestamp));
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    private final AnomalyDetector anomalyDetector = new AnomalyDetector();

    public AnalyticsEngine() {
        // Task dọn dữ liệu cũ định kỳ
        scheduler.scheduleAtFixedRate(() -> {
            for (MetricStore store : metrics.values()) {
                store.expireOldData();
            }
        }, 1, 1, TimeUnit.SECONDS);
    }

    public void ingest(Event event) {
        metrics.computeIfAbsent(event.getType(), k -> new MetricStore()).addEvent(event);
        eventQueue.offer(event);
    }

    public List<String> queryTopK(String type, int k) {
        MetricStore store = metrics.get(type);
        return (store != null) ? store.getTopK().getTopK(k) : Collections.emptyList();
    }

    public boolean detectAnomaly(String type) {
        MetricStore store = metrics.get(type);
        return (store != null) && anomalyDetector.isAnomalous(store.getStats());
    }
}
