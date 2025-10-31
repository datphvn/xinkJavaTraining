package com.bigdata.processors;

import com.bigdata.core.DataProcessor;
import com.bigdata.model.DataRecord;
import com.bigdata.model.RawRecord;
import com.bigdata.model.ValidatedRecord;
import com.bigdata.util.PerformanceMetrics;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.stream.Stream;

public class TransformationProcessor implements DataProcessor<ValidatedRecord, DataRecord> {
    private final PerformanceMetrics metrics;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public TransformationProcessor(PerformanceMetrics metrics) {
        this.metrics = metrics;
    }

    @Override
    public Stream<DataRecord> process(Stream<ValidatedRecord> input) {
        return input
                .map(this::transformToDataRecord)
                .filter(Optional::isPresent)
                .map(Optional::get);
    }

    private Optional<DataRecord> transformToDataRecord(ValidatedRecord record) {
        RawRecord raw = record.getRawRecord();
        try {
            // Mapping fields (assuming CSV structure: 0=timestamp, 1=category, 2=value)
            LocalDateTime timestamp = LocalDateTime.parse(raw.getField(0), FORMATTER);
            String category = raw.getField(1);
            double value = Double.parseDouble(raw.getField(2));

            return Optional.of(new DataRecord(category, value, timestamp));

        } catch (Exception e) {
            // Log transformation errors (e.g., unexpected data type or format)
            metrics.logError(e, "TRANSFORMATION_ERROR_LINE_" + raw.getLineNum());
            return Optional.empty();
        }
    }
}