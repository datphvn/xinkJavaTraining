package com.pipeline.mastery.main;

import com.pipeline.mastery.model.Record;
import com.pipeline.mastery.pipeline.Processor;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws IOException {
        String filePath = "mock_data.csv";

        System.out.println("--- Bắt đầu chạy Pipeline ---");

        // 1. Chạy Sequential (để làm nóng cache)
        System.out.println("\n[1] CHẠY SEQUENTIAL LẦN 1 (BẮT BUỘC TÍNH TOÁN)");
        Processor.process(filePath, false);

        // 2. Chạy Sequential Lần 2 (Tận dụng Memoization)
        System.out.println("\n[2] CHẠY SEQUENTIAL LẦN 2 (TẬN DỤNG CACHE)");
        Map<String, List<?>> sequentialResults = Processor.process(filePath, false);

        // 3. Chạy Parallel
        System.out.println("\n[3] CHẠY PARALLEL");
        Map<String, List<?>> parallelResults = Processor.process(filePath, true);

        // 4. Hiển thị kết quả cuối cùng
        System.out.println("\n--- DỮ LIỆU ĐÃ XỬ LÝ (Mẫu) ---");
        @SuppressWarnings("unchecked")
        List<Record> finalRecords = (List<Record>) sequentialResults.get("ValidRecords");

        finalRecords.stream()
                .limit(5)
                .forEach(record -> System.out.printf("ID: %d, Name: %s, Value: %.2f, Status: %s%n",
                        record.id(), record.name(), record.value(), record.status()));

        System.out.println("\n--- THÔNG TIN LỖI ---");
        List<?> invalidMessages = sequentialResults.get("InvalidValidationMessages");
        List<?> rawInvalidLines = sequentialResults.get("RawInvalidLines");

        invalidMessages.forEach(msg -> System.out.println("[Lỗi Validate]: " + msg));
        rawInvalidLines.forEach(line -> System.out.println("[Lỗi Parse Thô]: " + line));
    }
}
