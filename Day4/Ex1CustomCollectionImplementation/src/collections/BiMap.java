package collections;

import java.util.*;

public class BiMap<K,V> {
    private final Map<K,V> keyToVal = new HashMap<>();
    private final Map<V,K> valToKey = new HashMap<>();

    public void put(K key, V value) {
        if (keyToVal.containsKey(key)) valToKey.remove(keyToVal.get(key));
        if (valToKey.containsKey(value)) keyToVal.remove(valToKey.get(value));
        keyToVal.put(key,value);
        valToKey.put(value,key);
    }

    public V getValue(K key) { return keyToVal.get(key); }
    public K getKey(V value) { return valToKey.get(value); }
    public void removeByKey(K key) {
        V v = keyToVal.remove(key);
        if (v != null) valToKey.remove(v);
    }
    public void removeByValue(V value) {
        K k = valToKey.remove(value);
        if (k != null) keyToVal.remove(k);
    }
}
