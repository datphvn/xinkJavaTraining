import java.util.*;


public class Row {
    private final long id;
    private final Map<String, Object> values = new HashMap<>();

    public Row(long id) { this.id = id; }
    public long getId() { return id; }
    public void set(String col, Object val) { values.put(col, val); }
    public Object get(String col) { return values.get(col); }
    public Map<String,Object> asMap() { return Collections.unmodifiableMap(values); }
    public Row shallowCopyWithNewId(long newId) {
        Row r = new Row(newId);
        r.values.putAll(this.values);
        return r;
    }
    @Override public String toString() { return "Row{" + id + ", " + values + "}"; }
}
