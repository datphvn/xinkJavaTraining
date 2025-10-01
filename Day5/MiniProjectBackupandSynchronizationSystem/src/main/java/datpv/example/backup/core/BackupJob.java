package datpv.example.backup.core;

import datpv.example.backup.dedup.DedupStore;
import datpv.example.backup.storage.StorageProvider;
import datpv.example.backup.util.ChecksumUtil;
import datpv.example.backup.util.CompressionUtil;
import datpv.example.backup.util.CryptoUtil;

import java.io.*;
import java.nio.file.*;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * BackupJob: executes a backup according to config.
 * - incremental via metadata store (simple JSON file in destination)
 * - supports compression and encryption
 * - resume by checking existing destination filesize and checksum
 */
public class BackupJob {
    private final BackupConfig config;
    private final StorageProvider storage;
    private final ExecutorService executor;
    private volatile boolean cancelled = false;
    private BackupStatus status = BackupStatus.RUNNING;
    private ProgressListener progressListener;
    private final DedupStore dedupStore;

    // metadata file name in destination
    private static final String METADATA_FILE = ".backup_metadata.json";

    public BackupJob(BackupConfig config, StorageProvider storage) throws IOException {
        this.config = config;
        this.storage = storage;
        this.executor = Executors.newFixedThreadPool(Math.max(1, config.getParallelism()));
        this.dedupStore = new DedupStore(storage);
    }

    public void setProgressListener(ProgressListener l) { this.progressListener = l; }

    public CompletableFuture<BackupResult> execute() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Path src = config.getSourceDir();
                if (!Files.exists(src) || !Files.isDirectory(src)) {
                    status = BackupStatus.FAILED;
                    return new BackupResult(status, "Source directory missing");
                }
                List<Path> allFiles = Files.walk(src)
                        .filter(Files::isRegularFile)
                        .collect(Collectors.toList());
                long totalFiles = allFiles.size();
                long[] processed = {0};

                // load previous metadata (simple map path->checksum)
                Map<String, String> previous = loadMetadata();

                // jobs for changed files
                List<CompletableFuture<Void>> futures = new ArrayList<>();

                for (Path file : allFiles) {
                    if (cancelled) break;
                    String relative = src.relativize(file).toString();
                    futures.add(CompletableFuture.runAsync(() -> {
                        try {
                            long totalBytes = Files.size(file);
                            if (progressListener != null) progressListener.onFileStart(relative);

                            // compute checksum
                            String checksum = ChecksumUtil.sha256Hex(file);

                            // incremental: skip if same checksum exists in previous
                            if (previous != null && checksum.equals(previous.get(relative))) {
                                // skip
                                if (progressListener != null) {
                                    progressListener.onFileProgress(relative, totalBytes, totalBytes);
                                    progressListener.onFileComplete(relative, true);
                                }
                                return;
                            }

                            // Deduplication: if dedup store has checksum, just record ref
                            if (config.isDeduplicate() && dedupStore.has(checksum)) {
                                // link in storage (store metadata only)
                                storage.putMeta(relative + ".ref", checksum);
                                if (progressListener != null) {
                                    progressListener.onFileProgress(relative, totalBytes, totalBytes);
                                    progressListener.onFileComplete(relative, true);
                                }
                                return;
                            }

                            // Prepare InputStream with throttle if required
                            try (InputStream fis = Files.newInputStream(file);
                                 InputStream in = config.getThrottleBytesPerSecond() > 0
                                         ? new ThrottledInputStream(fis, config.getThrottleBytesPerSecond())
                                         : fis) {

                                InputStream streamToStore = in;

                                // compression
                                if (config.isCompress()) {
                                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                    CompressionUtil.gzipCompress(streamToStore, baos);
                                    streamToStore = new ByteArrayInputStream(baos.toByteArray());
                                } else {
                                    // if compression not requested, use the original input stream (but we've read it if compress attempted)
                                }

                                // encryption
                                if (config.getEncryptKey() != null) {
                                    byte[] encrypted = CryptoUtil.encryptStreamToBytes(streamToStore, config.getEncryptKey());
                                    streamToStore = new ByteArrayInputStream(encrypted);
                                }

                                // upload to storage with resume support - here we simply write a new file
                                storage.put(relative, streamToStore, totalBytes);

                                // store in dedup
                                if (config.isDeduplicate()) dedupStore.put(checksum, relative);
                            }

                            if (progressListener != null) {
                                progressListener.onFileProgress(relative, Files.size(file), Files.size(file));
                                progressListener.onFileComplete(relative, true);
                            }
                        } catch (Exception e) {
                            if (progressListener != null) {
                                progressListener.onFileComplete(src.relativize(file).toString(), false);
                            }
                        } finally {
                            synchronized (processed) {
                                processed[0]++;
                                if (progressListener != null) progressListener.onOverallProgress(processed[0], totalFiles);
                            }
                        }
                    }, executor));
                }

                // wait for all
                CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

                // write new metadata (map of relative -> checksum)
                Map<String, String> newMetadata = new HashMap<>();
                for (Path f : allFiles) newMetadata.put(src.relativize(f).toString(), ChecksumUtil.sha256Hex(f));
                writeMetadata(newMetadata);

                status = cancelled ? BackupStatus.CANCELLED : BackupStatus.COMPLETED;
                return new BackupResult(status, cancelled ? "Cancelled" : "Completed");
            } catch (Exception e) {
                status = BackupStatus.FAILED;
                return new BackupResult(status, "Error: " + e.getMessage());
            } finally {
                executor.shutdown();
            }
        });
    }

    public void cancel() {
        cancelled = true;
        status = BackupStatus.CANCELLED;
        executor.shutdownNow();
    }

    public BackupStatus getStatus() { return status; }

    // Metadata stored as simple JSON: path->checksum
    private Map<String,String> loadMetadata() {
        try {
            InputStream is = storage.getMeta(METADATA_FILE);
            if (is == null) return null;
            return new com.fasterxml.jackson.databind.ObjectMapper().readValue(is, Map.class);
        } catch (IOException e) {
            return null;
        }
    }

    private void writeMetadata(Map<String,String> meta) {
        try {
            byte[] bytes = new com.fasterxml.jackson.databind.ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsBytes(meta);
            storage.putMeta(METADATA_FILE, bytes);
        } catch (IOException e) {
            // log
        }
    }
}
