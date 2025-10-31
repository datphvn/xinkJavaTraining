package com.etl.transforms;

import java.util.stream.Stream;
import java.util.function.Function;

// Thư viện các hàm Transform có thể cấu hình (Requirement 2)
public class TransformationLibrary {

    // Transform: Chuyển chuỗi thành chữ hoa
    public static Function<String, String> toUpperCase() {
        return String::toUpperCase;
    }

    // Transform: Thêm trường tính toán
    public static Function<String, String> addCalculatedField() {
        // Giả định dữ liệu là "ID:x,NAME:y,..."
        return record -> {
            if (record.contains("SALARY")) {
                try {
                    String salaryStr = record.substring(record.indexOf("SALARY:") + 7).split(",")[0];
                    double salary = Double.parseDouble(salaryStr);
                    double bonus = salary * 0.1;
                    return record + ",BONUS:" + String.format("%.2f", bonus);
                } catch (Exception e) {
                    // Bỏ qua lỗi parsing, trả về bản ghi gốc
                    return record;
                }
            }
            return record;
        };
    }

    // Transform: Lọc bản ghi cũ hơn
    public static Function<Stream<String>, Stream<String>> filterOldRecords() {
        // Trong thực tế sẽ dựa trên Timestamp
        return stream -> {
            System.out.println("Applying filter: remove records containing 'Charlie'");
            return stream.filter(record -> !record.contains("Charlie"));
        };
    }
}