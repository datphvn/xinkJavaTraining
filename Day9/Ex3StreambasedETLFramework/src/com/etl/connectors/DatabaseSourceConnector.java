package com.etl.connectors;

import com.etl.core.DataConnector;
import com.etl.model.ETLConfig;

import java.sql.*;
import java.util.function.Function;
import java.util.stream.Stream;

// Mock Connector đọc DB (E)
public class DatabaseSourceConnector implements DataConnector<String> {
    private ETLConfig config;

    @Override
    public void configure(ETLConfig config) {
        this.config = config;
    }

    // Mock Extraction: Không thực sự kết nối DB (do hạn chế thư viện)
    @Override
    public Stream<String> extract() throws SQLException {
        String query = config.getProperty("db.query");
        if (query == null) {
            throw new SQLException("DB query not configured.");
        }

        // Mocking: Trả về một Stream mô phỏng dữ liệu DB
        System.out.println("Executing mock query: " + query);

        // Giả lập 5 bản ghi từ DB
        return Stream.of(
                "ID:1,NAME:Alice,SALARY:50000",
                "ID:2,NAME:Bob,SALARY:65000",
                "ID:3,NAME:Charlie,SALARY:70000",
                "ID:4,NAME:David,SALARY:INVALID", // Dữ liệu lỗi
                "ID:5,NAME:Eve,SALARY:55000"
        );
    }
}