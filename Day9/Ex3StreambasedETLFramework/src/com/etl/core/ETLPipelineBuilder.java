package com.etl.core;

import com.etl.model.ETLResult;
import com.etl.transforms.QualityMonitor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class ETLPipelineBuilder<T> {

    private Stream<T> source;
    private List<Function<Stream<T>, Stream<T>>> transformations = new ArrayList<>();
    private final List<Consumer<T>> sinks = new ArrayList<>();
    private final QualityMonitor<T> monitor = new QualityMonitor<>(); // Monitoring Module (Requirement 3)
    private boolean incrementalEnabled = false; // Requirement 7

    public static <T> ETLPipelineBuilder<T> from(Stream<T> source) {
        ETLPipelineBuilder<T> builder = new ETLPipelineBuilder<>();
        builder.source = source;
        return builder;
    }

    public ETLPipelineBuilder<T> enableIncremental(boolean enabled) {
        this.incrementalEnabled = enabled;
        return this;
    }

    // Configurable transformation (Requirement 2)
    public ETLPipelineBuilder<T> transform(Function<Stream<T>, Stream<T>> transformation) {
        transformations.add(transformation);
        return this;
    }

    // Validation (Part of Requirement 3)
    public ETLPipelineBuilder<T> validate(Predicate<T> validator, String ruleName) {
        return transform(stream -> stream.filter(item -> {
            boolean valid = validator.test(item);
            if (!valid) {
                monitor.logInvalidRecord(item, ruleName);
            }
            return valid;
        }));
    }

    // Transform with type change (map)
    public <R> ETLPipelineBuilder<R> map(Function<T, R> mapper) {
        // Áp dụng các phép biến đổi đã có trên stream hiện tại
        Stream<T> intermediateStream = source;
        for (Function<Stream<T>, Stream<T>> transform : transformations) {
            intermediateStream = transform.apply(intermediateStream);
        }

        // Tạo builder mới với stream đã được map
        ETLPipelineBuilder<R> newBuilder = new ETLPipelineBuilder<>();
        newBuilder.source = intermediateStream.map(mapper);
        // Reset transformations (vì T đã đổi thành R)
        this.transformations.clear();

        return newBuilder;
    }

    // Load (L)
    public ETLPipelineBuilder<T> to(Consumer<T> sink) {
        sinks.add(sink);
        return this;
    }

    // Execute
    public ETLResult execute() {
        Stream<T> pipeline = source;

        // 1. Áp dụng tất cả các phép biến đổi
        for (Function<Stream<T>, Stream<T>> transform : transformations) {
            pipeline = transform.apply(pipeline);
        }

        // 2. Tùy chỉnh cho Incremental Processing (Mock: chỉ lấy 1000 bản ghi mới nhất)
        if (incrementalEnabled) {
            // Trong thực tế, logic này sẽ phức tạp hơn, dựa trên timestamp/sequence number
            System.out.println("Processing incrementally (Limiting to 1000 records)...");
            pipeline = pipeline.limit(1000);
        }

        // 3. Terminal Operation (Load + Error Handling/Monitoring)
        AtomicLong processedCount = new AtomicLong(0);

        long startTime = System.currentTimeMillis();

        try {
            pipeline
                    .peek(item -> processedCount.incrementAndGet())
                    .forEach(item -> { // Terminal Operation
                        for (Consumer<T> sink : sinks) {
                            try {
                                sink.accept(item);
                            } catch (Exception e) {
                                monitor.logProcessingError(e, item); // Log Load Error (Requirement 4)
                            }
                        }
                    });
        } catch (Exception e) {
            // Pipeline thất bại hoàn toàn (ví dụ: lỗi trong Extraction hoặc Transformation)
            return ETLResult.failure(e, processedCount.get(), monitor.getErrorCount());
        } finally {
            // In báo cáo chất lượng dữ liệu
            monitor.printQualityReport();
        }

        long duration = System.currentTimeMillis() - startTime;

        // Trả về kết quả cuối cùng
        return ETLResult.success(processedCount.get(), monitor.getValidCount(), duration);
    }
}