// file: customcol/CustomMap.java
package customcol;
import java.util.Collection;
import java.util.Set;
public interface CustomMap<K,V> {
    V put(K key, V value);
    V get(K key);
    V remove(K key);
    boolean containsKey(K key);
    int size();
    boolean isEmpty();
    Set<K> keySet();
    Collection<V> values();
}
