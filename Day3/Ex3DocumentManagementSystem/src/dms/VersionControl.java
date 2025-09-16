package dms;

import java.time.Instant;
import java.util.*;

public class VersionControl {
    public static class Version {
        public final String id;
        public final Instant timestamp;
        public final String contentSnapshot;
        public final String message;

        public Version(String contentSnapshot, String message) {
            this.id = UUID.randomUUID().toString();
            this.timestamp = Instant.now();
            this.contentSnapshot = contentSnapshot;
            this.message = message;
        }
    }

    private final List<Version> versions = new ArrayList<>();
    private final Document owner; // composition: VC belongs to Document

    public VersionControl(Document owner) {
        this.owner = owner;
    }

    public void commit(String message) {
        versions.add(new Version(owner.getContent(), message));
    }

    public List<Version> getHistory() {
        return Collections.unmodifiableList(versions);
    }

    public Optional<Version> getVersionById(String id) {
        return versions.stream().filter(v -> v.id.equals(id)).findFirst();
    }

    public void rollbackTo(String versionId) {
        Optional<Version> v = getVersionById(versionId);
        if (!v.isPresent()) throw new IllegalArgumentException("Version not found");
        // update document content (directly) and make a new commit note
        owner.content = v.get().contentSnapshot;
        commit("Rollback to " + versionId);
    }
}
