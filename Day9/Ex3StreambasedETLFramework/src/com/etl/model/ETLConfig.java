package com.etl.model;

import java.util.Map;

// Đối tượng cấu hình cho các Connector/Transform
public class ETLConfig {
    private final String connectorType;
    private final Map<String, String> properties;

    public ETLConfig(String connectorType, Map<String, String> properties) {
        this.connectorType = connectorType;
        this.properties = properties;
    }

    public String getConnectorType() { return connectorType; }
    public String getProperty(String key) { return properties.get(key); }

    // TBD: Thêm logic Schema (ví dụ: Map<String, DataType>) để hỗ trợ Schema Evolution
}