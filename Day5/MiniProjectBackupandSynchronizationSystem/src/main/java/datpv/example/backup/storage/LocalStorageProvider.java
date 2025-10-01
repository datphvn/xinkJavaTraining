package datpv.example.backup.storage;

import java.io.*;
import java.nio.file.*;
import java.util.Objects;

/**
 * Local filesystem provider (simple).
 */
public class LocalStorageProvider implements StorageProvider {
    private final Path base;

    public LocalStorageProvider(Path base) throws IOException {
        this.base = base;
        Files.createDirectories(base);
    }

    @Override
    public void put(String key, InputStream data, long expectedSize) throws IOException {
        Path out = base.resolve(key).normalize();
        Files.createDirectories(out.getParent());
        // write to temp then move for atomicity
        Path tmp = Files.createTempFile(base, "upload-", ".tmp");
        try (OutputStream os = Files.newOutputStream(tmp, StandardOpenOption.TRUNCATE_EXISTING)) {
            byte[] buf = new byte[8192];
            int r;
            while ((r = data.read(buf)) != -1) os.write(buf, 0, r);
            os.flush();
        }
        Files.move(tmp, out, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
    }

    @Override
    public void putMeta(String key, byte[] data) throws IOException {
        Path out = base.resolve("_meta").resolve(key);
        Files.createDirectories(out.getParent());
        Files.write(out, data, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    @Override
    public void putMeta(String key, String value) throws IOException {
        putMeta(key, value.getBytes());
    }

    @Override
    public InputStream getMeta(String key) throws IOException {
        Path p = base.resolve("_meta").resolve(key);
        if (!Files.exists(p)) return null;
        return Files.newInputStream(p);
    }

    @Override
    public boolean exists(String key) throws IOException {
        return Files.exists(base.resolve(key));
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
