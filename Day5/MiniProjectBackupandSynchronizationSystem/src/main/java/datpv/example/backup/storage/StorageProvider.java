package datpv.example.backup.storage;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

/**
 * Abstract storage provider.
 */
public interface StorageProvider {
    /**
     * Put a file into provider storage at given key/path.
     * Implementations should overwrite atomically.
     */
    void put(String key, InputStream data, long expectedSize) throws IOException;

    /**
     * Put metadata bytes into a provider-managed metadata key.
     */
    void putMeta(String key, byte[] data) throws IOException;

    /**
     * Put a metadata reference (string).
     */
    void putMeta(String key, String value) throws IOException;

    /**
     * Get metadata InputStream (if exists), otherwise null.
     */
    InputStream getMeta(String key) throws IOException;

    /**
     * Check if key exists
     */
    boolean exists(String key) throws IOException;

    /**
     * For dedup: store mapping of checksum->storedKey
     */
    void putReference(String checksum, String storedKey) throws IOException;

    boolean hasReference(String checksum) throws IOException;
}
