package datpv.example.backup.sync;

public class SyncOptions {
    private boolean bidirectional = true;
    private boolean overwriteNewer = true;

    public enum ConflictPolicy {
        KEEP_NEWEST,
        KEEP_BOTH
    }

    private ConflictPolicy conflictPolicy = ConflictPolicy.KEEP_NEWEST;

    public boolean isBidirectional() {
        return bidirectional;
    }

    public void setBidirectional(boolean bidirectional) {
        this.bidirectional = bidirectional;
    }

    public boolean isOverwriteNewer() {
        return overwriteNewer;
    }

    public void setOverwriteNewer(boolean overwriteNewer) {
        this.overwriteNewer = overwriteNewer;
    }

    public ConflictPolicy getConflictPolicy() {
        return conflictPolicy;
    }

    public void setConflictPolicy(ConflictPolicy conflictPolicy) {
        this.conflictPolicy = conflictPolicy;
    }

    @Override
    public String toString() {
        return "SyncOptions{" +
                "bidirectional=" + bidirectional +
                ", overwriteNewer=" + overwriteNewer +
                ", conflictPolicy=" + conflictPolicy +
                '}';
    }
}
