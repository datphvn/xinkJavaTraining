package com.pipeline.mastery.pipeline;

import com.pipeline.mastery.interfaces.*;
import com.pipeline.mastery.model.Record;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Processor {
    private static final String DELIMITER = "\\|";
    private static final Map<String, BigDecimal> VALUE_CACHE = new ConcurrentHashMap<>();

    // --- 1. CSV Parsing (Functional: ThrowingFunction.lift) ---
    private static final Function<String, Optional<Record>> CSV_PARSER =
            ThrowingFunction.lift(line -> {
                String[] parts = line.split(DELIMITER);
                if (parts.length != 4) {
                    throw new IllegalArgumentException("Invalid column count: " + line);
                }
                // Bỏ qua dòng tiêu đề
                if ("ID".equalsIgnoreCase(parts[0])) {
                    throw new IllegalArgumentException("Header line skipped");
                }

                // Ép kiểu: có thể ném NumberFormatException
                int id = Integer.parseInt(parts[0].trim());
                String name = parts[1].trim();
                BigDecimal value = new BigDecimal(parts[2].trim());
                String status = parts[3].trim();

                return new Record(id, name, value, status);
            });

    // --- 2. Validation Rules (Functional: DataValidator composition) ---
    private static final DataValidator<Record> VALIDATE_ID =
            record -> record.id() > 0
                    ? ValidationResult.valid()
                    : ValidationResult.invalid("ID must be positive.");

    private static final DataValidator<Record> VALIDATE_NAME_LENGTH =
            record -> record.name().length() >= 3
                    ? ValidationResult.valid()
                    : ValidationResult.invalid("Name must be at least 3 characters.");

    private static final DataValidator<Record> VALIDATE_VALUE_RANGE =
            record -> record.value().compareTo(BigDecimal.ZERO) > 0 &&
                    record.value().compareTo(new BigDecimal(10000)) < 0
                    ? ValidationResult.valid()
                    : ValidationResult.invalid("Value is out of valid range (0, 10000).");

    // Composable Rule (AND)
    private static final DataValidator<Record> FULL_VALIDATOR =
            VALIDATE_ID.and(VALIDATE_NAME_LENGTH).and(VALIDATE_VALUE_RANGE);


    // --- 3. Memoization for Expensive Calculation ---
    // Giả sử tính toán này rất tốn kém
    private static final Function<BigDecimal, BigDecimal> EXPENSIVE_TAX_CALCULATOR =
            value -> {
                try {
                    // Giả lập tính toán mất 10ms
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                // Thuế 10%
                return value.multiply(new BigDecimal("0.10"));
            };

    // Memoized Function
    private static final Function<BigDecimal, BigDecimal> MEMOIZED_TAX_CALCULATOR =
            value -> VALUE_CACHE.computeIfAbsent(value.toPlainString(),
                    k -> EXPENSIVE_TAX_CALCULATOR.apply(value));


    // --- 4. Transformation Stages (Functional: DataTransformer composition) ---
    // Stage 1: Thêm phí dịch vụ (1%)
    private static final DataTransformer<Record, Record> ADD_SERVICE_FEE =
            record -> record.withValue(record.value().multiply(new BigDecimal("1.01")));

    // Stage 2: Tính và thêm thuế (Sử dụng Memoization)
    private static final DataTransformer<Record, Record> ADD_TAX_AND_UPDATE_STATUS =
            record -> {
                BigDecimal tax = MEMOIZED_TAX_CALCULATOR.apply(record.value());
                BigDecimal newValue = record.value().add(tax);
                // Cập nhật trạng thái nếu giá trị vượt ngưỡng
                String newStatus = newValue.compareTo(new BigDecimal("300")) > 0 ? "HIGH_VALUE" : record.status();
                return record.withValue(newValue).withStatus(newStatus);
            };

    // Composable Pipeline (AND THEN)
    private static final DataTransformer<Record, Record> FULL_TRANSFORMER =
            ADD_SERVICE_FEE.withLogging("ServiceFee").andThen(
                    ADD_TAX_AND_UPDATE_STATUS.withLogging("TaxAndStatus"));


    // --- 5. Main Processing Function (HOF) ---
    public static Map<String, List<?>> process(String filePath, boolean parallel) throws IOException {
        long startTime = System.nanoTime();

        // 1. Read lines from file
        try (Stream<String> lines = Files.lines(Paths.get(filePath))) {

            Stream<String> stream = parallel ? lines.parallel() : lines;

            // 2. Separate processing for valid and invalid records
            Map<Boolean, List<String>> partitionedLines = stream
                    .collect(Collectors.partitioningBy(line -> line.split(DELIMITER).length == 4 && !"ID".equalsIgnoreCase(line.split(DELIMITER)[0])));

            List<String> rawInvalidRecords = partitionedLines.get(false);
            List<String> rawValidRecords = partitionedLines.get(true);

            // 3. Process Valid Records: Parse -> Validate -> Transform
            List<Record> validRecords = new ArrayList<>();
            List<String> invalidValidationRecords = new ArrayList<>();

            rawValidRecords.stream()
                    .map(CSV_PARSER) // Optional<Record>
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .forEach(record -> {
                        ValidationResult validationResult = FULL_VALIDATOR.validate(record);
                        if (validationResult.isValid()) {
                            // Apply Transformation Pipeline
                            validRecords.add(FULL_TRANSFORMER.apply(record));
                        } else {
                            // Bổ sung chi tiết lỗi
                            invalidValidationRecords.add(String.format("ID %d: %s", record.id(), validationResult.message()));
                        }
                    });

            // 4. Result Aggregation and Performance Monitoring
            long endTime = System.nanoTime();
            double durationMs = (endTime - startTime) / 1_000_000.0;

            System.out.printf("--- PROCESSING SUMMARY (%s) ---%n", parallel ? "PARALLEL" : "SEQUENTIAL");
            System.out.printf("Total Execution Time: %.2f ms%n", durationMs);
            System.out.printf("Processed Records: %d%n", validRecords.size());
            System.out.printf("Validation Failures: %d%n", invalidValidationRecords.size());
            System.out.printf("Parsing Errors (Raw Invalid): %d%n", rawInvalidRecords.size());
            System.out.printf("Memoization Cache Size: %d%n", VALUE_CACHE.size());
            System.out.println("------------------------------------");

            // 5. Return Results (dùng Map để trả về kết quả cấu trúc)
            Map<String, List<?>> results = new HashMap<>();
            results.put("ValidRecords", validRecords);
            results.put("InvalidValidationMessages", invalidValidationRecords);
            results.put("RawInvalidLines", rawInvalidRecords);
            return results;

        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            throw e;
        }
    }
}
