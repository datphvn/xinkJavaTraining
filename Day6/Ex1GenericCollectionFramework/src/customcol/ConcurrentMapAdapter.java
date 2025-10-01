// file: customcol/ConcurrentMapAdapter.java
package customcol;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentMapAdapter<K,V> implements CustomMap<K,V> {
    private final ConcurrentHashMap<K,V> map = new ConcurrentHashMap<>();

    @Override public V put(K key, V value) { return map.put(key, value); }
    @Override public V get(K key) { return map.get(key); }
    @Override public V remove(K key) { return map.remove(key); }
    @Override public boolean containsKey(K key) { return map.containsKey(key); }
    @Override public int size() { return map.size(); }
    @Override public boolean isEmpty() { return map.isEmpty(); }
    @Override public Set<K> keySet() { return map.keySet(); }
    @Override public Collection<V> values() { return map.values(); }
}
