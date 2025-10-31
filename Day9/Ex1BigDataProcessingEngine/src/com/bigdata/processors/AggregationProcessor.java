package com.bigdata.processors;

import com.bigdata.model.DataRecord;
import com.bigdata.model.Statistics;
import com.bigdata.model.TimeSeriesData;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.DoubleSummaryStatistics;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// Note: This is an analytical processor, designed for post-ETL analysis
public class AggregationProcessor {

    // Aggregation using teeing for multiple stats in one pass
    public Map<String, Statistics> aggregateByCategory(Stream<DataRecord> records) {
        return records
                .filter(record -> record.getCategory() != null)
                .collect(Collectors.groupingBy(
                        DataRecord::getCategory,
                        // Use Collectors.teeing for concurrent calculation of count and summary stats
                        Collectors.teeing(
                                Collectors.counting(),
                                Collectors.summarizingDouble(DataRecord::getValue),
                                Statistics::new
                        )
                ));
    }

    // Time-series aggregation (e.g., calculating average over a fixed window)
    public Map<LocalDateTime, Double> aggregateByTimeWindow(
            Stream<TimeSeriesData> data,
            Duration windowSize) {

        return data
                .collect(Collectors.groupingBy(
                        record -> truncateToWindow(record.getTimestamp(), windowSize),
                        TreeMap::new,  // Ensures time windows are sorted
                        Collectors.averagingDouble(TimeSeriesData::getValue)
                ));
    }

    // Helper to snap a timestamp to the start of a defined window
    private LocalDateTime truncateToWindow(LocalDateTime timestamp, Duration windowSize) {
        long seconds = windowSize.getSeconds();
        long truncatedSeconds = (timestamp.toEpochSecond(ZoneOffset.UTC) / seconds) * seconds;
        return LocalDateTime.ofEpochSecond(truncatedSeconds, 0, ZoneOffset.UTC);
    }
}