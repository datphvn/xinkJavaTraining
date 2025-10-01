// file: customcol/SimpleMap.java
package customcol;

import java.util.*;
public class SimpleMap<K,V> implements CustomMap<K,V> {

    private static class Entry<K,V> {
        final K key;
        V value;
        Entry(K k, V v) { key = k; value = v; }
    }

    private final CustomList<Entry<K,V>> entries = new CustomArrayList<>();

    @Override
    public V put(K key, V value) {
        for (int i = 0; i < entries.size(); i++) {
            Entry<K,V> e = entries.get(i);
            if (Objects.equals(e.key, key)) {
                V old = e.value;
                e.value = value;
                return old;
            }
        }
        entries.add(new Entry<>(key, value));
        return null;
    }

    @Override
    public V get(K key) {
        for (int i = 0; i < entries.size(); i++) {
            Entry<K,V> e = entries.get(i);
            if (Objects.equals(e.key, key)) return e.value;
        }
        return null;
    }

    @Override
    public V remove(K key) {
        for (int i = 0; i < entries.size(); i++) {
            Entry<K,V> e = entries.get(i);
            if (Objects.equals(e.key, key)) {
                V val = e.value;
                entries.remove(i);
                return val;
            }
        }
        return null;
    }

    @Override
    public boolean containsKey(K key) {
        return get(key) != null;
    }

    @Override
    public int size() { return entries.size(); }

    @Override
    public boolean isEmpty() { return entries.isEmpty(); }

    @Override
    public Set<K> keySet() {
        Set<K> s = new HashSet<>();
        for (int i = 0; i < entries.size(); i++) s.add(entries.get(i).key);
        return s;
    }

    @Override
    public Collection<V> values() {
        List<V> vals = new ArrayList<>();
        for (int i = 0; i < entries.size(); i++) vals.add(entries.get(i).value);
        return vals;
    }
}
