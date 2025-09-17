import java.util.*;

public class Schema {
    private final List<Column> columns = new ArrayList<>();
    private final Map<String, Column> byName = new HashMap<>();

    public void addColumn(String name, DataType type) {
        Column c = new Column(name, type);
        columns.add(c);
        byName.put(name, c);
    }

    public Column get(String name) { return byName.get(name); }
    public List<Column> getColumns() { return Collections.unmodifiableList(columns); }
    public boolean hasColumn(String name) { return byName.containsKey(name); }
}
