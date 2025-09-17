import java.util.*;


public class Transaction {
    public enum OpType {INSERT, UPDATE, DELETE}
    public static class Op {
        final OpType type;
        final Table table;
        final Row rowBefore; // for update/delete: snapshot before; for insert: null
        final Row rowAfter;  // for insert: inserted row; update: after copy? not needed for rollback

        Op(OpType type, Table table, Row before, Row after) {
            this.type = type; this.table = table; this.rowBefore = before; this.rowAfter = after;
        }
    }

    private final List<Op> ops = new ArrayList<>();
    private boolean active = true;
    void logInsert(Table t, Row inserted) { ops.add(new Op(OpType.INSERT, t, null, inserted)); }
    void logDelete(Table t, Row deleted) { ops.add(new Op(OpType.DELETE, t, deleted, null)); }
    void logUpdate(Table t, Row beforeSnapshot) { ops.add(new Op(OpType.UPDATE, t, beforeSnapshot, null)); }

    public void commit() { active = false; ops.clear(); }
    public void rollback() {
        // reverse operations in reverse order
        ListIterator<Op> it = ops.listIterator(ops.size());
        while (it.hasPrevious()) {
            Op op = it.previous();
            switch (op.type) {
                case INSERT:
                    // remove inserted row
                    op.table.internalRemoveRow(op.rowAfter.getId());
                    break;
                case DELETE:
                    // re-insert deleted row
                    op.table.internalInsertRow(op.rowBefore);
                    break;
                case UPDATE:
                    // restore previous values: remove current row and insert previous snapshot
                    op.table.internalRemoveRow(op.rowBefore.getId());
                    op.table.internalInsertRow(op.rowBefore);
                    break;
            }
        }
        active = false;
        ops.clear();
    }
    public boolean isActive() { return active; }
}
