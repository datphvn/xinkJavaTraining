package com.bigdata.core;

import com.bigdata.model.*;
import com.bigdata.processors.*;
import com.bigdata.io.*;
import com.bigdata.util.*;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.*;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

public class BigDataEngine {

    private final PerformanceMetrics metrics = new PerformanceMetrics();
    private final int parallelismLevel;

    public BigDataEngine(int parallelismLevel) {
        this.parallelismLevel = parallelismLevel;
    }

    // Core method to run the ETL pipeline
    public void runPipeline(Path inputPath, Path outputPath, OutputFormatter<DataRecord> formatter) {
        metrics.start();

        // --- 1. Define Pipeline Stages ---
        List<ValidationRule<RawRecord>> rules = List.of(
                r -> r.getField(1) != null && !r.getField(1).isEmpty(),
                r -> { try { Double.parseDouble(r.getField(2)); return true; } catch (Exception e) { return false; }}
        );
        DataProcessor<RawRecord, ValidatedRecord> validationStage = new ValidationProcessor(rules);
        DataProcessor<ValidatedRecord, DataRecord> transformationStage = new TransformationProcessor(metrics);

        DataProcessor<RawRecord, DataRecord> fullProcessor = validationStage
                .andThen(transformationStage)
                .withErrorHandling(e -> metrics.logError(e, "RUNTIME_EXCEPTION_IN_PIPELINE"));

        // Khai báo ForkJoinPool bên ngoài để có thể gọi shutdown trong finally
        ForkJoinPool customPool = null;

        try {
            // Khởi tạo ForkJoinPool (Không dùng try-with-resources)
            customPool = new ForkJoinPool(parallelismLevel);

            // --- 2. Process Large File in Parallel ---
            Stream<DataRecord> resultStream = customPool.submit(() ->
                    FileChunker.streamFile(inputPath)   // Source (Memory-efficient, lazy)
                            .parallel()                     // Enable parallel processing
                            .flatMap(rawRecord -> {
                                // Track progress on the source records
                                metrics.recordProcessed(1);
                                return fullProcessor.process(Stream.of(rawRecord));
                            })
            ).get(); // .get() chờ kết quả từ tác vụ (Task)

            // --- 3. Terminal Operation: Output (Load) ---
            AtomicLong outputCount = new AtomicLong(0);

            try (var writer = Files.newBufferedWriter(outputPath, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {

                // Use forEachOrdered to preserve file order despite parallel processing
                resultStream
                        .forEachOrdered(record -> {
                            try {
                                writer.write(formatter.format(record) + "\n");
                                outputCount.incrementAndGet();
                            } catch (IOException e) {
                                throw new UncheckedIOException(e); // Propagate IO failure
                            }
                        });
            }

            System.out.printf("✅ Successfully wrote %d clean records to %s%n", outputCount.get(), outputPath);

        } catch (Exception e) {
            metrics.logError(e, "FATAL_PIPELINE_ERROR");
        } finally {
            // Đảm bảo ForkJoinPool được đóng lại một cách thủ công
            if (customPool != null) {
                customPool.shutdown();
            }
            metrics.stop();
            metrics.printMetrics();
        }
    }

    public static void main(String[] args) throws IOException {
        Path inputPath = Paths.get("big_data_input.csv");

        // Prepare dummy data (100,000 lines)
        FileChunker.createDummyFile(inputPath, 100_000);

        // Initialize engine with CPU core count
        int cores = Runtime.getRuntime().availableProcessors();
        BigDataEngine engine = new BigDataEngine(cores);

        // Run with CSV output
        engine.runPipeline(inputPath, Paths.get("processed_output.csv"), new CsvFormatter());
    }
}