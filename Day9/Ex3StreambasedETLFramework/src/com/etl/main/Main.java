package com.etl.main;

import com.etl.connectors.DatabaseSourceConnector;
import com.etl.core.DataConnector;
import com.etl.core.ETLPipelineBuilder;
import com.etl.model.ETLConfig;
import com.etl.model.ETLResult;
import com.etl.transforms.TransformationLibrary;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) throws Exception {

        // 1. Cấu hình Connector
        ETLConfig dbConfig = new ETLConfig("DB", Map.of(
                "db.url", "jdbc:h2:mem:test",
                "db.query", "SELECT id, name, salary FROM users WHERE status='ACTIVE'"
        ));

        // 2. Chọn Connector
        DataConnector<String> dbConnector = new DatabaseSourceConnector();
        dbConnector.configure(dbConfig);

        // 3. Extraction
        Stream<String> rawDataStream = dbConnector.extract();

        // 4. Định nghĩa Sink (Load)
        Consumer<String> consoleSink = record -> {
            // Mock Load: Giả lập lỗi Load cho bản ghi có ID:4
            if (record.contains("ID:4")) {
                throw new RuntimeException("DB Connection timeout during Load for ID:4");
            }
            System.out.println("LOADED: " + record);
        };

        // 5. Xây dựng Pipeline (T, L)
        ETLResult result = ETLPipelineBuilder.from(rawDataStream)
                // (T) Transform 1: Lọc dữ liệu cũ
                .transform(TransformationLibrary.filterOldRecords())

                // (T) Transform 2: Thêm trường tính toán
                .transform(stream -> stream.map(TransformationLibrary.addCalculatedField()))

                // (T) Validation: Kiểm tra trường SALARY có phải là số (Data Quality)
                .validate(record -> {
                    try {
                        String salaryStr = record.substring(record.indexOf("SALARY:") + 7).split(",")[0];
                        Double.parseDouble(salaryStr);
                        return true;
                    } catch (Exception e) {
                        return false; // Invalid SALARY
                    }
                }, "SALARY_IS_NUMERIC")

                // (L) Load: Ghi vào console
                .to(consoleSink)

                // (Optional) Kích hoạt Incremental
                .enableIncremental(true)

                // Thực thi
                .execute();

        // 6. Hiển thị kết quả cuối cùng
        System.out.println("\n" + result);
    }
}