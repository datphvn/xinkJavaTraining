package com.bigdata.model;

import java.time.LocalDateTime;
import java.util.DoubleSummaryStatistics;

// Giả định dữ liệu đầu vào từ CSV
public class RawRecord {
    private final int lineNum;
    private final String[] fields;

    public RawRecord(int lineNum, String[] fields) {
        this.lineNum = lineNum;
        this.fields = fields;
    }

    public int getLineNum() { return lineNum; }
    public String getField(int index) {
        return (fields != null && index < fields.length) ? fields[index] : null;
    }
    public String[] getFields() { return fields; }
}

