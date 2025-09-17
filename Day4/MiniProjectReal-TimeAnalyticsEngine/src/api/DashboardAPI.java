package api;

import analytics.AnalyticsEngine;

public class DashboardAPI {
    private final AnalyticsEngine engine;

    public DashboardAPI(AnalyticsEngine engine) {
        this.engine = engine;
    }

    // RESTful API giả lập
    public void queryTopEvents() {
        System.out.println("Top events: " + engine.queryTopK("page_view", 5));
    }
}
