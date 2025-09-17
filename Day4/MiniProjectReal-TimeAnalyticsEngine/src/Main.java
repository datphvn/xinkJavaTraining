import analytics.AnalyticsEngine;
import analytics.Event;

import java.util.Random;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        AnalyticsEngine engine = new AnalyticsEngine();
        Random rand = new Random();

        String[] eventTypes = {"user_login", "page_view", "purchase", "comment"};


        long end = System.currentTimeMillis() + 5000;
        while (System.currentTimeMillis() < end) {
            String type = eventTypes[rand.nextInt(eventTypes.length)];
            double value = switch (type) {
                case "purchase" -> rand.nextInt(100);
                case "page_view" -> 1;
                case "comment" -> 1;
                default -> 1;
            };

            Event e = new Event(type, System.currentTimeMillis(), value);
            engine.ingest(e);


            System.out.println("📥 Ingested: " + type + " (value=" + value + ")");

            Thread.sleep(200);
        }


        System.out.println("\n=== 📊 Phân tích sau 5 giây ===");
        for (String type : eventTypes) {
            System.out.println("Top sự kiện " + type + ": " + engine.queryTopK(type, 3));
            System.out.println("Anomaly " + type + "? " + engine.detectAnomaly(type));
        }
    }
}
