package com.etl.transforms;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

// Data Quality Monitoring (Requirement 3, 4)
public class QualityMonitor<T> {

    private final ConcurrentHashMap<String, AtomicLong> ruleViolationCounts = new ConcurrentHashMap<>();
    private final AtomicLong processingErrorCount = new AtomicLong(0);
    private final AtomicLong validCount = new AtomicLong(0);

    // Ghi lại bản ghi không vượt qua Validation Rule
    public void logInvalidRecord(T record, String ruleName) {
        ruleViolationCounts
                .computeIfAbsent(ruleName, k -> new AtomicLong(0))
                .incrementAndGet();
        // Không tăng validCount
    }

    // Ghi lại lỗi xảy ra trong quá trình Load (ví dụ: lỗi ghi DB, lỗi mạng)
    public void logProcessingError(Exception e, T item) {
        processingErrorCount.incrementAndGet();
        System.err.println("LOAD ERROR - Item: " + item + ", Error: " + e.getMessage());
    }

    // Ghi lại bản ghi hợp lệ (được gọi sau khi qua tất cả các bước validation/transform)
    public void incrementValidCount() {
        validCount.incrementAndGet();
    }

    // Báo cáo
    public void printQualityReport() {
        System.out.println("\n--- ETL Quality Report ---");
        System.out.println("Total Valid Records (Passed Pipeline): " + validCount.get());
        System.out.println("Total Load/Sink Errors: " + processingErrorCount.get());
        System.out.println("--- Validation Violations ---");
        ruleViolationCounts.forEach((rule, count) -> {
            System.out.printf("Rule '%s' violated %d times%n", rule, count.get());
        });
        System.out.println("----------------------------");
    }

    public long getErrorCount() {
        return processingErrorCount.get();
    }

    public long getValidCount() {
        // Đây là số bản ghi đã qua được tất cả các bước transform và validation
        // Trong ETLBuilder.execute(), chúng ta dùng processedCount làm tổng số bản ghi đi qua sink
        return validCount.get();
    }
}