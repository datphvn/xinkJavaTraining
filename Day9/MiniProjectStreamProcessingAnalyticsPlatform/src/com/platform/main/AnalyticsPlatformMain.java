package com.platform.main;

import com.platform.dashboard.AnalyticsDashboard;
import com.platform.ingestion.DataIngestionEngine;
import com.platform.model.DataRecord;
import com.platform.model.Anomaly;
import com.platform.model.Metric;
import com.platform.monitor.PerformanceMonitor;
import com.platform.processing.StreamProcessingEngine;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AnalyticsPlatformMain {

    /**
     * Phương thức khởi động nền tảng xử lý Stream Analytics.
     */
    public static void main(String[] args) throws IOException, InterruptedException {

        System.out.println("=================================================");
        System.out.println("🚀 ANALYTICS PLATFORM: STARTING STREAM PROCESSING");
        System.out.println("=================================================");

        // Khởi tạo các thành phần hệ thống
        DataIngestionEngine ingestion = new DataIngestionEngine();
        StreamProcessingEngine processing = new StreamProcessingEngine();
        AnalyticsDashboard dashboard = new AnalyticsDashboard();
        PerformanceMonitor monitor = new PerformanceMonitor(dashboard);

        // --- PHASE 1: BATCH PROCESSING (Dữ liệu Lịch sử) ---
        // Pipeline này sẽ chạy đồng bộ và thu thập kết quả vào List
        List<DataRecord> historicalData = processHistoricalData(ingestion, processing, monitor);

        // --- PHASE 2: REAL-TIME STREAM PROCESSING ---
        // Pipeline này sẽ chạy không đồng bộ (bằng ExecutorService)
        ExecutorService rtExecutor = startRealTimeProcessing(ingestion, processing, monitor, dashboard);

        // Giữ luồng chính chạy trong 10 giây để quan sát Real-time metrics
        System.out.println("\n[MAIN THREAD] Chờ 10 giây để quan sát Real-time Metrics...");
        try {
            Thread.sleep(10000);
        } finally {
            rtExecutor.shutdownNow(); // Đảm bảo dừng luồng xử lý Real-time
        }

        // --- PHASE 3: ANALYTICS VÀ REPORTING (Sau khi dừng Real-time) ---
        System.out.println("\n=================================================");
        System.out.println("📊 FINAL REPORTING");
        System.out.println("=================================================");

        // 1. Interactive Queries (Dùng dữ liệu lịch sử)
        dashboard.executeQuery("Find Top 5 Users by Average Value", historicalData);

        // 2. Performance Analysis
        System.out.println(monitor.analyzePerformance());

        System.out.println("✅ PLATFORM DEMO COMPLETED.");
    }

    private static List<DataRecord> processHistoricalData(
            DataIngestionEngine ingestion,
            StreamProcessingEngine processing,
            PerformanceMonitor monitor) throws IOException {

        Path mockFilePath = Path.of("historical_data.csv");
        createMockCSVFile(mockFilePath, 5000);

        System.out.println("--- 1. BATCH PROCESSING (Historical) ---");

        Stream<DataRecord> batchStream = ingestion.readCSV(mockFilePath);

        Stream<DataRecord> processedBatchStream =
                pipe(batchStream, s -> monitor.monitorThroughput(s, "Batch_E"));

        processedBatchStream =
                pipe(processedBatchStream, processing::clean);

        processedBatchStream =
                pipe(processedBatchStream, processing::enrich);

        processedBatchStream =
                pipe(processedBatchStream, s -> processing.detectAnomalies(s).map(a -> {
                    System.out.println("ANOMALY DETECTED: " + a.toString());

                    return new DataRecord(
                            "Anomaly_Detector",
                            a.getKey(),
                            a.getCurrentValue(),
                            Map.of("status", "ANOMALY", "reason", a.getReason())
                    );
                }));

        processedBatchStream =
                pipe(processedBatchStream, s -> monitor.monitorLatency(s, "Batch_T"));

        // Giả lập Load: Collect thành List
        return processedBatchStream
                .peek(System.out::println)
                .collect(Collectors.toList());
    }

    private static ExecutorService startRealTimeProcessing(
            DataIngestionEngine ingestion,
            StreamProcessingEngine processing,
            PerformanceMonitor monitor,
            AnalyticsDashboard dashboard) {

        System.out.println("\n--- 2. REAL-TIME PROCESSING (Kafka Mock) ---");

        Stream<DataRecord> realTimeSource = ingestion.readFromKafka("events_topic");

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(() -> {

            // REAL-TIME Pipeline
            Stream<DataRecord> rtStream = pipe(realTimeSource, s -> monitor.monitorLatency(s, "RealTime_E"));
            rtStream = pipe(rtStream, processing::clean);

            Stream<Metric> aggregatedStream = processing.aggregate(rtStream, Duration.ofSeconds(5));

            // LOAD: Gửi Metrics đến Dashboard (Sử dụng forEach và bọc Metric)
            aggregatedStream.forEach(metric -> {
                dashboard.updateMetrics(Stream.of(metric));
            });
        });

        return executor; // Trả về Executor để có thể quản lý (shutdown)
    }

    // Helper để đơn giản hóa việc nối chuỗi các Stream methods
    private static <T> Stream<T> pipe(Stream<T> stream, Function<Stream<T>, Stream<T>> transformation) {
        return transformation.apply(stream);
    }

    private static void createMockCSVFile(Path path, int lines) throws IOException {
        try (var writer = Files.newBufferedWriter(path)) {
            writer.write("key,value,location\n");
            Random rnd = new Random();
            for (int i = 0; i < lines; i++) {
                String key = "User_" + (i % 100);
                double value = 15.0 + rnd.nextGaussian() * 8.0 + (i % 100 == 0 ? 50.0 : 0.0);
                String location = "Zone_" + (i % 5);
                writer.write(String.format("%s,%.2f,%s\n", key, value, location));
            }
        }
    }
}