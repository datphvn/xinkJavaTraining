package datpv.example.loganalyzer.stats;

import datpv.example.loganalyzer.model.LogEntry;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class LogStatistics {
    private final AtomicLong total = new AtomicLong(0);
    private final AtomicLong errorCount = new AtomicLong(0);
    private final Map<String, AtomicLong> levelCounts = new ConcurrentHashMap<>();
    private final Map<String, AtomicLong> messageCounts = new ConcurrentHashMap<>();
    private final List<Long> responseTimes = Collections.synchronizedList(new ArrayList<>());

    public void accept(LogEntry entry) {
        total.incrementAndGet();
        String level = entry.getLevel() == null ? "INFO" : entry.getLevel().toUpperCase();
        levelCounts.computeIfAbsent(level, k -> new AtomicLong()).incrementAndGet();
        messageCounts.computeIfAbsent(entry.getMessage(), k -> new AtomicLong()).incrementAndGet();
        if ("ERROR".equalsIgnoreCase(level) || "WARN".equalsIgnoreCase(level)) {
            errorCount.incrementAndGet();
        }
        Object rt = entry.getAttributes().get("response_time");
        if (rt instanceof Number) responseTimes.add(((Number) rt).longValue());
        // also try common attribute names
        if (entry.getAttributes().get("response_time_ms") instanceof Number)
            responseTimes.add(((Number) entry.getAttributes().get("response_time_ms")).longValue());
    }

    public long getTotal() { return total.get(); }
    public long getErrorCount() { return errorCount.get(); }

    public Map<String, Long> getLevelCounts() {
        return levelCounts.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().get()));
    }

    public List<Map.Entry<String, Long>> topMessages(int n) {
        return messageCounts.entrySet().stream()
                .map(e -> Map.entry(e.getKey(), e.getValue().get()))
                .sorted((a,b) -> Long.compare(b.getValue(), a.getValue()))
                .limit(n)
                .collect(Collectors.toList());
    }

    public OptionalDouble averageResponseTime() {
        synchronized (responseTimes) {
            return responseTimes.stream().mapToLong(Long::longValue).average();
        }
    }

    public double errorRate() {
        long t = total.get();
        return t == 0 ? 0.0 : (double) errorCount.get() / t;
    }
}
