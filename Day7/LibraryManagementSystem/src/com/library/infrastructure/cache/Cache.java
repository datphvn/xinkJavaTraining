package com.library.infrastructure.cache;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class Cache<K, V> {
    private final Map<K, CacheEntry<V>> cache = new ConcurrentHashMap<>();
    private final long ttlMillis;

    public Cache(long ttlMillis) {
        this.ttlMillis = ttlMillis;
    }

    public void put(K key, V value) {
        cache.put(key, new CacheEntry<>(value, Instant.now().toEpochMilli()));
    }

    public Optional<V> get(K key) {
        CacheEntry<V> entry = cache.get(key);
        if (entry == null) return Optional.empty();

        long age = Instant.now().toEpochMilli() - entry.timestamp;
        if (age > ttlMillis) {
            cache.remove(key);
            return Optional.empty();
        }
        return Optional.of(entry.value);
    }

    public void invalidate(K key) {
        cache.remove(key);
    }

    public int size() { return cache.size(); }

    private static class CacheEntry<V> {
        V value;
        long timestamp;
        CacheEntry(V value, long timestamp) {
            this.value = value;
            this.timestamp = timestamp;
        }
    }
}
