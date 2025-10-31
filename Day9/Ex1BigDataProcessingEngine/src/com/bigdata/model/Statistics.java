package com.bigdata.model;

import java.util.DoubleSummaryStatistics;

public class Statistics {
    private final long count;
    private final DoubleSummaryStatistics summaryStats;

    public Statistics(long count, DoubleSummaryStatistics summaryStats) {
        this.count = count;
        this.summaryStats = summaryStats;
    }
    public long getCount() { return count; }
    public double getAverage() { return summaryStats.getAverage(); }
    public double getSum() { return summaryStats.getSum(); }
}
