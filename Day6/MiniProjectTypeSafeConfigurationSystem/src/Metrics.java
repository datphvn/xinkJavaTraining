public class Metrics {
    private static long startTime = System.currentTimeMillis();

    public static void record(String action) {
        long elapsed = System.currentTimeMillis() - startTime;
        System.out.println("[METRICS] " + action + " after " + elapsed + " ms");
    }
}
