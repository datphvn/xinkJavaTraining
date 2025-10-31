package com.realtime.analytics;

import com.realtime.model.Event;
import com.realtime.store.EventStore;
import com.realtime.store.MetricStore;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WindowCalculator {
    private final EventStore eventStore;
    private final MetricStore metricStore;

    public WindowCalculator(EventStore eventStore, MetricStore metricStore) {
        this.eventStore = eventStore;
        this.metricStore = metricStore;
    }

    // Last 5 minutes sliding window average calculation
    public void calculateAvgWindow(int minutes) {
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        LocalDateTime start = now.minusMinutes(minutes);

        Stream<Event> recentEvents = eventStore.getEventsInWindow(start, now);

        // Grouping and Averaging: Multi-metric calculation in one stream pass
        Map<String, Double> avgValues = recentEvents
                .filter(event -> event.getValue() != null)
                .collect(Collectors.groupingBy(
                        Event::getType,
                        Collectors.averagingDouble(Event::getValue)
                ));

        // Update metrics with window information
        avgValues.forEach((type, avg) -> {
            String metricName = String.format("AVG_%s_%dM", type, minutes);
            metricStore.updateWindowMetric(metricName, avg);
            System.out.printf("Window Metric [%s]: %.2f%n", metricName, avg);
        });
    }

    // Anomaly detection: Volume drop check
    public void detectAnomaly(int minutes) {
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        LocalDateTime start = now.minusMinutes(minutes);

        // Sử dụng Stream.count()
        long totalEvents = eventStore.getEventsInWindow(start, now).count();

        // Simple Anomaly: If total event count drops significantly
        if (totalEvents < 5) {
            System.err.println("🚨 ANOMALY ALERT: Total event volume dropped significantly in the last " + minutes + " minutes. Count: " + totalEvents);
        }
    }
}