package com.realtime.store;

import com.realtime.model.Metric;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// Mô phỏng API Endpoint, sử dụng String.format và Map.toString() thay vì JSON
public class DashboardAPI {
    private final MetricStore metricStore;

    public DashboardAPI(MetricStore metricStore) {
        this.metricStore = metricStore;
    }

    // Endpoint 1: Real-time counts (Sử dụng Map.toString)
    public String getRealTimeMetricsString() {
        return metricStore.getRealTimeCounts().toString();
    }

    // Endpoint 2: Windowed metrics (Sử dụng Map.toString)
    public String getWindowMetricsString() {
        return metricStore.getWindowMetrics().toString();
    }

    // Endpoint 3: Historical trend (Sử dụng Stream và String building)
    public String getHistoricalTrendString(String metricName) {
        // Stream filters history for a specific metric type
        List<String> recentMetrics = metricStore.metricHistory.stream()
                .filter(metric -> metric.getName().contains(metricName))
                .sorted((m1, m2) -> m2.getTimestamp().compareTo(m1.getTimestamp()))
                .limit(10)
                .map(Metric::toSimpleString)
                .collect(Collectors.toList());

        // Format the list as a simple array string
        return "[" + String.join(", ", recentMetrics) + "]";
    }
}