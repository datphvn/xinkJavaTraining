package com.etl.core;

import com.etl.model.ETLConfig;
import java.io.IOException;
import java.sql.SQLException;
import java.util.stream.Stream;

// Interface cho Extraction Layer (Requirement 1)
public interface DataConnector<T> {

    // Mở kết nối và trả về Stream dữ liệu
    Stream<T> extract() throws IOException, SQLException;

    // Thiết lập cấu hình cho Connector
    void configure(ETLConfig config);
}