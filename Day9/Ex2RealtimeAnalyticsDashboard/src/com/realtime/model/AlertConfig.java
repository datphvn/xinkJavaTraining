package com.realtime.model;

import java.util.function.Predicate;

public class AlertConfig {
    private final String metricName;
    private final Predicate<Double> threshold;
    private final String message;

    public AlertConfig(String metricName, Predicate<Double> threshold, String message) {
        this.metricName = metricName;
        this.threshold = threshold;
        this.message = message;
    }

    public String getMetricName() { return metricName; }
    public Predicate<Double> getThreshold() { return threshold; }
    public String getMessage() { return message; }
}