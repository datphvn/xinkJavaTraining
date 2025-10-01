package datpv.example.backup.storage;

import java.io.*;
import java.nio.file.*;

/**
 * Mock S3 provider stores objects in local directory (simulates cloud).
 */
public class MockS3Provider implements StorageProvider {
    private final Path bucket;

    public MockS3Provider(Path bucket) throws IOException {
        this.bucket = bucket;
        Files.createDirectories(bucket);
    }

    @Override
    public void put(String key, InputStream data, long expectedSize) throws IOException {
        Path out = bucket.resolve(key).normalize();
        Files.createDirectories(out.getParent());
        try (OutputStream os = Files.newOutputStream(out, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
            byte[] buf = new byte[8192];
            int r;
            while ((r = data.read(buf)) != -1) os.write(buf, 0, r);
        }
    }

    @Override
    public void putMeta(String key, byte[] data) throws IOException {
        Path out = bucket.resolve("_meta").resolve(key);
        Files.createDirectories(out.getParent());
        Files.write(out, data, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    @Override
    public void putMeta(String key, String value) throws IOException {
        putMeta(key, value.getBytes());
    }

    @Override
    public InputStream getMeta(String key) throws IOException {
        Path p = bucket.resolve("_meta").resolve(key);
        if (!Files.exists(p)) return null;
        return Files.newInputStream(p);
    }

    @Override
    public boolean exists(String key) throws IOException {
        return Files.exists(bucket.resolve(key));
    }

    @Override
    public void putReference(String checksum, String storedKey) throws IOException {
        putMeta("ref-" + checksum, storedKey);
    }

    @Override
    public boolean hasReference(String checksum) throws IOException {
        return getMeta("ref-" + checksum) != null;
    }
}
