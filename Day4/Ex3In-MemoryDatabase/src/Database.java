import java.util.*;
import java.util.stream.Collectors;

public class Database {
    private final Map<String, Table> tables = new HashMap<>();

    public void createTable(String name, Schema schema) {
        if (tables.containsKey(name)) throw new IllegalArgumentException("Table exists");
        tables.put(name, new Table(name, schema));
    }
    public Table getTable(String name) { return tables.get(name); }

    // Simple select: returns list of maps (column->value)
    public List<Map<String,Object>> select(String tableName, List<String> columns, Map<String,Object> whereEq) {
        Table t = getTable(tableName);
        if (t == null) return List.of();
        List<Row> rows = t.select(whereEq);
        return rows.stream().map(r -> {
            Map<String,Object> m = new LinkedHashMap<>();
            for (String c : columns) m.put(c, r.get(c));
            return m;
        }).collect(Collectors.toList());
    }

    // Join two tables on equality col1 (tableA) = col2 (tableB)
    public List<Map<String,Object>> join(String tableA, String tableB, String colA, String colB) {
        Table a = getTable(tableA);
        Table b = getTable(tableB);
        if (a == null || b == null) return List.of();
        List<Map<String,Object>> result = new ArrayList<>();
        // if b has index on colB, use it:
        Index idxB = null;
        try {
            java.lang.reflect.Field f = Table.class.getDeclaredField("indexes");
            f.setAccessible(true);
            @SuppressWarnings("unchecked")
            Map<String, Index> idxMap = (Map<String,Index>) f.get(b);
            idxB = idxMap.get(colB);
        } catch (Exception ignored) {}

        if (idxB != null) {
            for (Row ra : a.getAllRows()) {
                Object v = ra.get(colA);
                for (Long idb : idxB.find(v)) {
                    // reflectively get row from b
                    for (Row rb : b.getAllRows()) {
                        if (rb.getId() == idb) {
                            Map<String,Object> m = new LinkedHashMap<>();
                            m.putAll(ra.asMap()); m.putAll(rb.asMap());
                            result.add(m);
                        }
                    }
                }
            }
        } else {
            // nested loop
            for (Row ra : a.getAllRows()) {
                Object va = ra.get(colA);
                for (Row rb : b.getAllRows()) {
                    if (Objects.equals(va, rb.get(colB))) {
                        Map<String,Object> m = new LinkedHashMap<>();
                        m.putAll(ra.asMap()); m.putAll(rb.asMap());
                        result.add(m);
                    }
                }
            }
        }
        return result;
    }

    // Aggregation on a table column: function: COUNT, SUM, AVG, MIN, MAX
    public Object aggregate(String tableName, String column, String function, Map<String,Object> whereEq) {
        Table t = getTable(tableName);
        if (t == null) return null;
        List<Row> rows = t.select(whereEq);
        switch (function.toUpperCase()) {
            case "COUNT": return rows.size();
            case "SUM": {
                double s = 0; for (Row r: rows) { Number n = (Number) r.get(column); if (n!=null) s+=n.doubleValue(); } return s;
            }
            case "AVG": {
                double s=0; int c=0; for (Row r: rows) { Number n = (Number) r.get(column); if (n!=null) { s+=n.doubleValue(); c++; } } return c==0?null:s/c;
            }
            case "MIN": {
                Double min = null; for (Row r: rows) { Number n = (Number) r.get(column); if (n!=null) { double v=n.doubleValue(); min = (min==null||v<min)?v:min; } } return min;
            }
            case "MAX": {
                Double max = null; for (Row r: rows) { Number n = (Number) r.get(column); if (n!=null) { double v=n.doubleValue(); max = (max==null||v>max)?v:max; } } return max;
            }
            default: throw new IllegalArgumentException("Unknown function: " + function);
        }
    }

    // Transaction helper
    public Transaction beginTransaction() { return new Transaction(); }
}
