package collections;

import java.util.*;

public class MultiMap<K,V> {
    private final Map<K, List<V>> map = new HashMap<>();

    public void put(K key, V value) {
        map.computeIfAbsent(key, k -> new ArrayList<>()).add(value);
    }

    public List<V> get(K key) {
        return map.getOrDefault(key, Collections.emptyList());
    }

    public boolean remove(K key, V value) {
        List<V> list = map.get(key);
        return list != null && list.remove(value);
    }

    public void removeAll(K key) {
        map.remove(key);
    }
}
