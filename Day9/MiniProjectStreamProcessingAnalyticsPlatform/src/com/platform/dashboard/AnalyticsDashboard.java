package com.platform.dashboard;

import com.platform.model.DataRecord;
import com.platform.model.Metric;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AnalyticsDashboard {

    private final ConcurrentHashMap<String, Metric> realTimeMetrics = new ConcurrentHashMap<>();

    public AnalyticsDashboard() {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        // Giả lập WebSocket / Dashboard API
        scheduler.scheduleAtFixedRate(this::sendUpdatesToWebSocket, 0, 2, TimeUnit.SECONDS);
    }

    // Real-time metrics update (Load)
    public void updateMetrics(Stream<Metric> data) {
        data.forEach(metric -> realTimeMetrics.put(metric.name, metric));
    }

    // Định nghĩa các phương thức khác (Mock)
    public record QueryRequest(String query) {}
    public record QueryResult(List<DataRecord> results) {}
    public record ChartConfig(String type) {}
    public record ChartData(String data) {}

    public QueryResult executeQuery(QueryRequest request) {
        return new QueryResult(List.of());
    }

    // Interactive queries (Mock SQL-like query)
    public List<DataRecord> executeQuery(String query, List<DataRecord> historicalData) {
        System.out.println("\n--- Executing Query: " + query + " ---");

        // Giả lập truy vấn SQL-like (GROUP BY, ORDER BY, LIMIT)
        Map<String, Double> avgByKey = historicalData.stream()
                .collect(Collectors.groupingBy(
                        DataRecord::getKey,
                        Collectors.averagingDouble(DataRecord::getValue)
                ));

        return avgByKey.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(5)
                .map(entry -> {
                    System.out.printf("Query Result: Key=%s, Avg Value=%.2f%n", entry.getKey(), entry.getValue());
                    return new DataRecord("Query_Result", entry.getKey(), entry.getValue(), Map.of("Query", query));
                })
                .collect(Collectors.toList());
    }

    public ChartData prepareChart(Stream<DataRecord> data, ChartConfig config) {
        return new ChartData("Mock Chart Data");
    }

    private void sendUpdatesToWebSocket() {
        if (realTimeMetrics.isEmpty()) return;

        System.out.println("\n[DASHBOARD UPDATE] --- Real-time Metrics ---");
        realTimeMetrics.values().stream()
                .sorted(Comparator.comparing(m -> m.name))
                .limit(5)
                .forEach(System.out::println);
        System.out.println("-------------------------------------------");
    }
}