import java.util.*;


public class Index {
    private final String column;
    private final Map<Object, Set<Long>> index = new HashMap<>();

    public Index(String column) { this.column = column; }
    public void add(Object value, long rowId) {
        index.computeIfAbsent(value, k -> new HashSet<>()).add(rowId);
    }
    public void remove(Object value, long rowId) {
        Set<Long> s = index.get(value);
        if (s != null) { s.remove(rowId); if (s.isEmpty()) index.remove(value); }
    }
    public Set<Long> find(Object value) {
        return index.getOrDefault(value, Collections.emptySet());
    }
    public String getColumn() { return column; }
}
