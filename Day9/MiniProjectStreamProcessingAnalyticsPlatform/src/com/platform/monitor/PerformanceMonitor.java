package com.platform.monitor;

import com.platform.model.Metric;
import com.platform.dashboard.AnalyticsDashboard;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import java.util.stream.Stream;

public class PerformanceMonitor {
    private final AnalyticsDashboard dashboard;

    public PerformanceMonitor(AnalyticsDashboard dashboard) {
        this.dashboard = dashboard;
    }

    // Monitoring Throughput (Sử dụng peek)
    public <T> Stream<T> monitorThroughput(Stream<T> stream, String stageName) {
        AtomicLong counter = new AtomicLong(0);
        long startTime = System.nanoTime();

        return stream.peek(item -> {
            long currentCount = counter.incrementAndGet();
            if (currentCount % 1000 == 0) { // Cập nhật sau mỗi 1000 bản ghi
                long duration = System.nanoTime() - startTime;
                double throughput = (double) currentCount / (duration / 1_000_000_000.0);

                dashboard.updateMetrics(Stream.of(
                        new Metric("Throughput_" + stageName, throughput, "THROUGHPUT_RPS")
                ));
            }
        });
    }

    // Monitoring Latency (Sử dụng peek)
    public <T> Stream<T> monitorLatency(Stream<T> stream, String stageName) {
        return stream.peek(item -> {
            long startTime = System.nanoTime();
            // Latency được tính bằng thời gian giữa các phép toán.
            // Để đơn giản hóa, ta giả lập việc báo cáo sau khi xử lý xong một item.
            long latency = System.nanoTime() - startTime;
            dashboard.updateMetrics(Stream.of(
                    new Metric("Latency_" + stageName, latency / 1_000_000.0, "LATENCY_MS")
            ));
        });
    }

    // Định nghĩa các phương thức khác (Mock)
    public void monitorResourceUsage() {}
    public record OptimizationSuggestion(String suggestion) {}
    public List<OptimizationSuggestion> analyzePerformance() {
        return List.of(new OptimizationSuggestion("Convert object streams to primitive streams (mapToInt) for stages with high numerical computation."));
    }
}