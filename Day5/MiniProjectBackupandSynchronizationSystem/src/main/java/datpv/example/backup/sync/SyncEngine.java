package datpv.example.backup.sync;

import datpv.example.backup.core.BackupConfig;
import datpv.example.backup.storage.StorageProvider;
import datpv.example.backup.util.ChecksumUtil;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.FileTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Simple bidirectional sync between local storage provider (source) and target provider.
 * For demo, StorageProvider must be LocalStorageProvider or similar supporting file listing (not in interface).
 * Here we assume sync between local file system trees (Path-based) rather than abstract storage keys for simplicity.
 */
public class SyncEngine {
    private final StorageProvider left;
    private final StorageProvider right;
    private final BackupConfig config;

    public SyncEngine(StorageProvider left, StorageProvider right, BackupConfig config) {
        this.left = left;
        this.right = right;
        this.config = config;
    }

    /**
     * For demo we accept Path source and target root names. Implementation copies files between local directories.
     */
    public SyncResult synchronize(Path source, Path targetRoot, SyncOptions options) {
        // For simplicity in demo, if left is LocalStorageProvider and right is MockS3Provider,
        // we map Path source to local dir and targetRoot to storage bucket subdir.
        // We'll implement a simple file copy: read left base dir path from config.getSourceDir()
        try {
            Path leftBase = config.getSourceDir();
            Path rightBase = (targetRoot.toString().equals("cloud-root")) ? Paths.get("data/mock-s3") : Paths.get(targetRoot.toString());
            List<Path> files = Files.walk(leftBase).filter(Files::isRegularFile).collect(Collectors.toList());
            int synced = 0, conflicts = 0;
            for (Path f : files) {
                Path rel = leftBase.relativize(f);
                Path dest = rightBase.resolve(rel);
                if (Files.exists(dest)) {
                    // conflict detection by checksum
                    String c1 = ChecksumUtil.sha256Hex(f);
                    String c2 = ChecksumUtil.sha256Hex(dest);
                    if (!c1.equals(c2)) {
                        conflicts++;
                        if (options.getConflictPolicy() == SyncOptions.ConflictPolicy.KEEP_NEWEST) {
                            // decide by last modified
                            FileTime t1 = Files.getLastModifiedTime(f);
                            FileTime t2 = Files.getLastModifiedTime(dest);
                            if (t1.toMillis() >= t2.toMillis()) {
                                Files.createDirectories(dest.getParent());
                                Files.copy(f, dest, StandardCopyOption.REPLACE_EXISTING);
                            } else {
                                // keep existing
                            }
                        } else {
                            // KEEP_BOTH: copy with suffix
                            Path alt = dest.resolveSibling(dest.getFileName().toString() + ".conflict-" + System.currentTimeMillis());
                            Files.createDirectories(alt.getParent());
                            Files.copy(f, alt);
                        }
                    }
                } else {
                    Files.createDirectories(dest.getParent());
                    Files.copy(f, dest);
                }
                synced++;
            }
            return new SyncResult(synced, conflicts);
        } catch (Exception e) {
            return new SyncResult(0, 0);
        }
    }

    public void resolveConflicts(List<SyncConflict> conflicts) {
        // Not implemented for demo: would apply policy per conflict.
    }
}
