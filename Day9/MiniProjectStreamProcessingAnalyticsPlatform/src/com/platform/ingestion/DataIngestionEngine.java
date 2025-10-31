package com.platform.ingestion;

import com.platform.model.DataRecord;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

public class DataIngestionEngine {
    private static final Random RND = new Random();

    // --- Batch/File Sources ---

    // Đọc CSV (Sửa lại: Giả lập, sử dụng Files.lines)
    public Stream<DataRecord> readCSV(Path file) throws IOException {
        System.out.println("Ingesting data from CSV: " + file);
        return Files.lines(file)
                .skip(1)
                .map(line -> line.split(","))
                .filter(parts -> parts.length >= 3)
                .map(parts -> {
                    try {
                        String key = parts[0];
                        double value = Double.parseDouble(parts[1]);
                        Map<String, String> props = Map.of("location", parts[2]);
                        return new DataRecord("CSV_Source", key, value, props);
                    } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                        return null;
                    }
                })
                .filter(record -> record != null);
    }

    // Đọc từ Database (Sửa lại: Giả lập)
    public Stream<DataRecord> readFromDatabase(String query) {
        System.out.println("Ingesting data from DB: " + query);
        return Stream.iterate(0, i -> i + 1)
                .limit(1000)
                .map(i -> new DataRecord("DB_Source", "User_" + (i % 50), 10.0 + RND.nextDouble() * 50, Map.of("query", query)));
    }

    // --- Real-time Sources ---

    // Đọc từ Kafka (Sửa lại: Infinite Stream)
    public Stream<DataRecord> readFromKafka(String topic) {
        System.out.println("Starting real-time ingestion from topic: " + topic);
        return Stream.generate(() -> {
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            String key = "Sensor_" + RND.nextInt(10);
            double value = 20.0 + RND.nextGaussian() * 5;
            return new DataRecord("Kafka_Source", key, value, Map.of("topic", topic));
        });
    }

    // Định nghĩa các phương thức khác (Mock)
    public Stream<DataRecord> readJSON(Path file) { return Stream.empty(); }
    public Stream<DataRecord> readParquet(Path file) { return Stream.empty(); }
    public Stream<DataRecord> readFromAPI(String endpoint) { return Stream.empty(); }
    public Stream<DataRecord> readFromWebSocket(String url) { return readFromKafka(url); }
    public Stream<DataRecord> readFromFileWatch(Path directory) { return Stream.empty(); }
}