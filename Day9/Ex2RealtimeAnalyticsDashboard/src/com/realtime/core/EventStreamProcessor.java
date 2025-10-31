package com.realtime.core;

import com.realtime.analytics.AlertingEngine;
import com.realtime.analytics.WindowCalculator;
import com.realtime.model.AlertConfig;
import com.realtime.model.Event;
import com.realtime.store.EventStore;
import com.realtime.store.MetricStore;
import com.realtime.store.DashboardAPI;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class EventStreamProcessor {
    private final BlockingQueue<Event> eventQueue = new LinkedBlockingQueue<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(4);

    private final EventStore eventStore;
    private final MetricStore metricStore;
    private final WindowCalculator windowCalculator;
    private final AlertingEngine alertingEngine;
    private final DashboardAPI dashboardAPI;

    public EventStreamProcessor(EventStore eventStore, MetricStore metricStore, WindowCalculator windowCalculator, AlertingEngine alertingEngine, DashboardAPI dashboardAPI) {
        this.eventStore = eventStore;
        this.metricStore = metricStore;
        this.windowCalculator = windowCalculator;
        this.alertingEngine = alertingEngine;
        this.dashboardAPI = dashboardAPI;
    }

    public void startProcessing() {
        System.out.println("Starting Real-time Analytics Processor...");

        // 1. Process events in batches (Every 1 second)
        scheduler.scheduleAtFixedRate(this::processBatch, 0, 1, TimeUnit.SECONDS);

        // 2. Window-based analytics (Every 5 seconds)
        scheduler.scheduleAtFixedRate(this::calculateWindows, 0, 5, TimeUnit.SECONDS);

        // 3. Alerting (Every 5 seconds, sync with window calculation)
        scheduler.scheduleAtFixedRate(alertingEngine::checkAlerts, 2, 5, TimeUnit.SECONDS);

        // 4. Cleanup old data (Every 1 minute)
        scheduler.scheduleAtFixedRate(this::cleanup, 0, 1, TimeUnit.MINUTES);
    }

    public void ingest(Event event) {
        eventQueue.offer(event);
        eventStore.ingestEvent(event); // Store for window/history tracking
    }

    private void processBatch() {
        List<Event> batch = new ArrayList<>();
        eventQueue.drainTo(batch, 5000);

        if (batch.isEmpty()) return;

        // Stream processing: Count events by type (Real-time aggregation)
        Map<String, Long> eventCounts = batch.stream()
                .collect(Collectors.groupingBy(
                        Event::getType,
                        Collectors.counting()
                ));

        eventCounts.forEach(metricStore::updateRealTimeCount);
        alertingEngine.checkAlertsOnBatch(batch);

        System.out.printf("Batch Processed: %d events. Counts: %s%n", batch.size(), eventCounts);
    }

    private void calculateWindows() {
        windowCalculator.calculateAvgWindow(5);
        windowCalculator.detectAnomaly(5);
    }

    private void cleanup() {
        eventStore.cleanupOldEvents(LocalDateTime.now().minusHours(1));

        // Print mock API endpoint data
        System.out.println("Dashboard Data (Counts): " + dashboardAPI.getRealTimeMetricsString());
        System.out.println("Dashboard Data (Historical): " + dashboardAPI.getHistoricalTrendString("TRANSACTION"));
    }

    public static void main(String[] args) throws InterruptedException {
        // 1. Initialize Stores
        EventStore eventStore = new EventStore();
        MetricStore metricStore = new MetricStore();
        DashboardAPI dashboardAPI = new DashboardAPI(metricStore);

        // 2. Configure Alerts
        List<AlertConfig> alertConfigs = List.of(
                new AlertConfig("AVG_TRANSACTION_5M", avg -> avg < 5.0, "Average transaction value dropped below $5.00"),
                new AlertConfig("AVG_ERROR_5M", avg -> avg > 1.0, "Average error severity is too high")
        );

        // 3. Initialize Analytics Engine
        WindowCalculator calculator = new WindowCalculator(eventStore, metricStore);
        AlertingEngine alerter = new AlertingEngine(metricStore, alertConfigs);
        EventStreamProcessor processor = new EventStreamProcessor(eventStore, metricStore, calculator, alerter, dashboardAPI);

        processor.startProcessing();

        // 4. Simulate Continuous Ingestion
        ScheduledExecutorService ingestion = Executors.newSingleThreadScheduledExecutor();
        ingestion.scheduleAtFixedRate(() -> {
            processor.ingest(new Event("PAGE_VIEW", 0.0));
            processor.ingest(new Event("TRANSACTION", Math.random() * 15 + 1));

            if (Math.random() < 0.1) { // 10% chance of error
                processor.ingest(new Event("ERROR", Math.random() * 5.0));
            }
        }, 0, 100, TimeUnit.MILLISECONDS);

        // Keep running for demonstration
        Thread.sleep(30000);

        ingestion.shutdown();
        processor.scheduler.shutdownNow();
        System.out.println("Processing finished.");
    }
}