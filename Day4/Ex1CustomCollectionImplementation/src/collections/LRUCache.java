package collections;

import java.util.*;

public class LRUCache<K,V> {
    private final int capacity;
    private final Map<K,V> map;

    public LRUCache(int capacity) {
        this.capacity = capacity;
        this.map = new LinkedHashMap<>(capacity,0.75f,true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<K,V> eldest) {
                return size() > LRUCache.this.capacity;
            }
        };
    }
    public V get(K key) { return map.get(key); }
    public void put(K key, V value) { map.put(key,value); }
    public int size() { return map.size(); }
}
