package datpv.example.backup.dedup;

import datpv.example.backup.storage.StorageProvider;

import java.io.IOException;

/**
 * Content-addressable dedup store using storage provider metadata.
 * Simplified: we just store mapping checksum -> storedKey in storage meta
 */
public class DedupStore {
    private final StorageProvider storage;

    public DedupStore(StorageProvider storage) {
        this.storage = storage;
    }

    public boolean has(String checksum) throws IOException {
        return storage.hasReference(checksum);
    }

    public void put(String checksum, String storedKey) throws IOException {
        storage.putReference(checksum, storedKey);
    }
}
