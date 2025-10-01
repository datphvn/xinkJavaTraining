package datpv.example.backup.sync;

public class SyncResult {
    private final int synced;
    private final int conflicts;

    public SyncResult(int synced, int conflicts) {
        this.synced = synced;
        this.conflicts = conflicts;
    }

    public int getSynced() {
        return synced;
    }

    public int getConflicts() {
        return conflicts;
    }

    @Override
    public String toString() {
        return "SyncResult{" +
                "synced=" + synced +
                ", conflicts=" + conflicts +
                '}';
    }
}
