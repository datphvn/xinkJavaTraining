package com.realtime.store;

import com.realtime.model.Metric;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.List;

public class MetricStore {
    // Current (real-time) metrics
    private final ConcurrentHashMap<String, Long> realTimeCounts = new ConcurrentHashMap<>();
    // Window metrics (e.g., average over 5 minutes)
    private final ConcurrentHashMap<String, Double> windowMetrics = new ConcurrentHashMap<>();
    // Historical metric data points
    public final CopyOnWriteArrayList<Metric> metricHistory = new CopyOnWriteArrayList<>(); // public for DashboardAPI access

    public void updateRealTimeCount(String metricName, long count) {
        realTimeCounts.merge(metricName, count, Long::sum);
        metricHistory.add(new Metric("COUNT_" + metricName, (double) realTimeCounts.get(metricName)));
    }

    public void updateWindowMetric(String metricName, double value) {
        windowMetrics.put(metricName, value);
        metricHistory.add(new Metric("AVG_" + metricName, value));
    }

    // For Dashboard API
    public Map<String, Long> getRealTimeCounts() {
        return realTimeCounts;
    }

    public Map<String, Double> getWindowMetrics() {
        return windowMetrics;
    }
}