import java.util.*;
import java.util.concurrent.locks.*;

public class Table {
    private final String name;
    private final Schema schema;
    private final Map<Long, Row> rows = new LinkedHashMap<>();
    private long nextId = 1;
    private final Map<String, Index> indexes = new HashMap<>();
    private final ReadWriteLock rw = new ReentrantReadWriteLock();

    public Table(String name, Schema schema) {
        this.name = name; this.schema = schema;
    }

    public String getName() { return name; }

    // Create index on a column
    public void createIndex(String column) {
        if (!schema.hasColumn(column)) throw new IllegalArgumentException("No such column: " + column);
        rw.writeLock().lock();
        try {
            Index idx = new Index(column);
            for (Row r : rows.values()) {
                idx.add(r.get(column), r.getId());
            }
            indexes.put(column, idx);
        } finally { rw.writeLock().unlock(); }
    }

    // Insert row: values map column->value. Returns Row id.
    public long insert(Map<String,Object> values, Transaction tx) {
        rw.writeLock().lock();
        try {
            long id = nextId++;
            Row r = new Row(id);
            for (Column c : schema.getColumns()) {
                Object v = values.get(c.name);
                r.set(c.name, v);
            }
            rows.put(id, r);
            // update indexes
            for (Index idx : indexes.values()) idx.add(r.get(idx.getColumn()), id);
            // log for transaction
            if (tx != null) tx.logInsert(this, r);
            return id;
        } finally { rw.writeLock().unlock(); }
    }

    // Select rows matching whereEq (map of column->value). If whereEq null -> all.
    public List<Row> select(Map<String,Object> whereEq) {
        rw.readLock().lock();
        try {
            List<Row> result = new ArrayList<>();
            if (whereEq != null && whereEq.size() == 1) {
                // Optimize single equality using index if exists
                Map.Entry<String,Object> e = whereEq.entrySet().iterator().next();
                Index idx = indexes.get(e.getKey());
                if (idx != null) {
                    for (Long id : idx.find(e.getValue())) {
                        Row r = rows.get(id);
                        if (r != null) result.add(r);
                    }
                    return result;
                }
            }
            for (Row r : rows.values()) {
                if (matches(r, whereEq)) result.add(r);
            }
            return result;
        } finally { rw.readLock().unlock(); }
    }

    private boolean matches(Row r, Map<String,Object> whereEq) {
        if (whereEq == null || whereEq.isEmpty()) return true;
        for (Map.Entry<String,Object> e : whereEq.entrySet()) {
            Object val = r.get(e.getKey());
            if (!Objects.equals(val, e.getValue())) return false;
        }
        return true;
    }

    // Update matching rows with updates map; returns count updated.
    public int update(Map<String,Object> whereEq, Map<String,Object> updates, Transaction tx) {
        rw.writeLock().lock();
        try {
            List<Row> matched = select(whereEq);
            for (Row r : matched) {
                // log old copy for rollback
                if (tx != null) tx.logUpdate(this, r.shallowCopyWithNewId(r.getId()));
                // update indexes: remove old index entries and add new
                for (Index idx : indexes.values()) {
                    String col = idx.getColumn();
                    Object oldVal = r.get(col);
                    Object newVal = updates.containsKey(col) ? updates.get(col) : oldVal;
                    if (!Objects.equals(oldVal, newVal)) {
                        idx.remove(oldVal, r.getId());
                        idx.add(newVal, r.getId());
                    }
                }
                // apply updates
                for (Map.Entry<String,Object> u : updates.entrySet()) r.set(u.getKey(), u.getValue());
            }
            return matched.size();
        } finally { rw.writeLock().unlock(); }
    }

    // Delete matching rows; returns count deleted
    public int delete(Map<String,Object> whereEq, Transaction tx) {
        rw.writeLock().lock();
        try {
            List<Row> matched = select(whereEq);
            for (Row r : matched) {
                if (tx != null) tx.logDelete(this, r);
                rows.remove(r.getId());
                for (Index idx : indexes.values()) idx.remove(r.get(idx.getColumn()), r.getId());
            }
            return matched.size();
        } finally { rw.writeLock().unlock(); }
    }

    public Schema getSchema() { return schema; }
    public Collection<Row> getAllRows() {
        rw.readLock().lock();
        try { return new ArrayList<>(rows.values()); } finally { rw.readLock().unlock(); }
    }

    // Internal utility used by Transaction rollback
    void internalInsertRow(Row r) {
        rw.writeLock().lock();
        try {
            rows.put(r.getId(), r);
            for (Index idx : indexes.values()) idx.add(r.get(idx.getColumn()), r.getId());
            nextId = Math.max(nextId, r.getId()+1);
        } finally { rw.writeLock().unlock(); }
    }
    void internalRemoveRow(long id) {
        rw.writeLock().lock();
        try {
            Row r = rows.remove(id);
            if (r != null) for (Index idx : indexes.values()) idx.remove(r.get(idx.getColumn()), id);
        } finally { rw.writeLock().unlock(); }
    }
}
