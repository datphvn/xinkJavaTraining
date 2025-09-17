package metrics;

import java.util.*;

public class TopKTracker<T> {
    private final Map<T, Integer> freq = new HashMap<>();

    public void increment(T item) {
        freq.put(item, freq.getOrDefault(item, 0) + 1);
    }

    public List<T> getTopK(int k) {
        return freq.entrySet().stream()
                .sorted((a, b) -> b.getValue() - a.getValue())
                .limit(k)
                .map(Map.Entry::getKey)
                .toList();
    }
}
