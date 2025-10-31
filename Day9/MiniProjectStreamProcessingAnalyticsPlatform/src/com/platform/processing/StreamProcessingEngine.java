package com.platform.processing;

import com.platform.model.Anomaly;
import com.platform.model.DataRecord;
import com.platform.model.Metric;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StreamProcessingEngine {

    // Records đơn giản hóa việc truyền dữ liệu bất biến
    public record MLModel(String name) {}
    public record Prediction(DataRecord record, double score) {}

    // Record tạm thời để chứa kết quả trung gian của teeing
    private record WindowStats(double average) {}

    private final Map<String, Double> historicalAverages = new ConcurrentHashMap<>();

    // ---------------- Transformation Operations ----------------

    public Stream<DataRecord> clean(Stream<DataRecord> input) {
        return input.filter(record -> record.getKey() != null && !record.getKey().isEmpty())
                .map(record -> new DataRecord(
                        record.getSourceId(),
                        record.getKey(),
                        Math.round(record.getValue() * 100.0) / 100.0,
                        record.getProperties()
                ));
    }

    public Stream<DataRecord> enrich(Stream<DataRecord> input) {
        return input.map(record -> {
            Map<String, String> newProps = new ConcurrentHashMap<>(record.getProperties());
            newProps.put("value_level", record.getValue() > 25.0 ? "HIGH" : "LOW");
            return new DataRecord(
                    record.getSourceId(), record.getKey(), record.getValue(), newProps
            );
        });
    }

    public Stream<DataRecord> normalize(Stream<DataRecord> input) {
        return input.map(record ->
                new DataRecord(
                        record.getSourceId(), record.getKey(), record.getValue() / 100.0, record.getProperties()
                ));
    }

    // ---------------- Aggregation and Windowing ----------------

    public Stream<Metric> aggregate(Stream<DataRecord> input, Duration window) {
        long windowSeconds = window.getSeconds();

        // Hàm cắt thời gian về đầu cửa sổ
        Function<DataRecord, LocalDateTime> windowTruncator = record -> {
            long epochSecond = record.getTimestamp().toEpochSecond(java.time.ZoneOffset.UTC);
            long truncatedSeconds = (epochSecond / windowSeconds) * windowSeconds;
            return LocalDateTime.ofEpochSecond(truncatedSeconds, 0, java.time.ZoneOffset.UTC);
        };

        // Sử dụng Collectors.groupingBy và Collectors.teeing
        return input
                .collect(Collectors.groupingBy(
                        record -> windowTruncator.apply(record).toString() + "_" + record.getKey(),
                        Collectors.teeing(
                                Collectors.counting(), // count
                                Collectors.averagingDouble(DataRecord::getValue), // avg
                                // Kết quả teeing: Trả về Record tạm thời chứa average
                                (count, avg) -> new WindowStats(avg)
                        )
                ))
                .entrySet()
                .stream()
                .map(entry -> {
                    // Tách key: [timestamp]_[key]
                    String key = entry.getKey().substring(entry.getKey().lastIndexOf("_") + 1);

                    // Lấy giá trị average từ Record tạm thời
                    double windowAvg = entry.getValue().average();

                    return new Metric(
                            String.format("AVG_%s_%d_sec", key, windowSeconds),
                            windowAvg, // Giá trị đã sửa lỗi
                            "WINDOW_AVG"
                    );
                });
    }

    // ---------------- Pattern Detection and ML ----------------

    public Stream<Anomaly> detectAnomalies(Stream<DataRecord> input) {
        double stdDev = 5.0; // Giả định độ lệch chuẩn cố định

        return input
                .map(record -> {
                    String key = record.getKey();
                    double currentValue = record.getValue();
                    double avg = historicalAverages.getOrDefault(key, 25.0); // Avg lịch sử

                    // Cập nhật Avg lịch sử (Incremental processing)
                    historicalAverages.merge(key, currentValue, (oldVal, newVal) -> (oldVal * 0.9) + (newVal * 0.1));

                    if (Math.abs(currentValue - avg) > (3 * stdDev)) {
                        return new Anomaly(
                                record.getTimestamp(), key, currentValue,
                                String.format("Value (%.2f) exceeds 3-sigma (Avg: %.2f)", currentValue, avg)
                        );
                    }
                    return null;
                })
                .filter(anomaly -> anomaly != null);
    }

    public Stream<Prediction> predict(Stream<DataRecord> input, MLModel model) {
        return input.map(record -> {
            double predictedScore = record.getValue() * 1.10;
            return new Prediction(record, predictedScore);
        });
    }
}