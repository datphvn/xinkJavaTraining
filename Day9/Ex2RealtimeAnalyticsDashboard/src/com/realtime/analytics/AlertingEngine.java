package com.realtime.analytics;

import com.realtime.model.AlertConfig;
import com.realtime.model.Event;
import com.realtime.store.MetricStore;

import java.util.List;

public class AlertingEngine {
    private final MetricStore metricStore;
    private final List<AlertConfig> configs;

    public AlertingEngine(MetricStore metricStore, List<AlertConfig> configs) {
        this.metricStore = metricStore;
        this.configs = configs;
    }

    public void checkAlerts() {
        // Kiểm tra các alerts dựa trên Window Metrics
        metricStore.getWindowMetrics().forEach((name, value) -> {
            configs.stream()
                    .filter(config -> name.contains(config.getMetricName())) // Match metric names
                    .filter(config -> config.getThreshold().test(value))     // Check if value violates threshold
                    .forEach(config -> {
                        System.out.printf("🔴 ALERT TRIGGERED: %s | Current Value: %.2f%n",
                                config.getMessage(), value);
                    });
        });
    }

    // Check based on immediate event batch characteristics
    public void checkAlertsOnBatch(List<Event> batch) {
        // Example: Alert if there are too many 'ERROR' events in a single batch
        long errorCount = batch.stream()
                .filter(event -> "ERROR".equals(event.getType()))
                .count();

        if (errorCount > 10) {
            System.err.printf("🔴 IMMEDIATE ALERT: Found %d errors in the latest batch!%n", errorCount);
        }
    }
}