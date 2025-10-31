package com.bigdata.io;

import com.bigdata.model.DataRecord;

import java.io.IOException;

public class CsvFormatter implements OutputFormatter<DataRecord> {

    @Override
    public String format(DataRecord record) throws IOException {
        // Simple comma-separated format
        return String.format("%s,%s,%.2f",
                record.getTimestamp().toString(),
                record.getCategory(),
                record.getValue());
    }
}