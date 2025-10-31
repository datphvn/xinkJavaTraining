package com.bigdata.io;

import com.bigdata.model.DataRecord;

import java.io.IOException;

public class ParquetFormatter implements OutputFormatter<DataRecord> {

    @Override
    public String format(DataRecord record) throws IOException {
        // Mock implementation since Parquet requires complex external libraries (Hadoop, Parquet-MR)
        return String.format("PARQUET_MOCK|Timestamp:%s|Category:%s|Value:%.2f",
                record.getTimestamp().toString(),
                record.getCategory(),
                record.getValue());
    }
}