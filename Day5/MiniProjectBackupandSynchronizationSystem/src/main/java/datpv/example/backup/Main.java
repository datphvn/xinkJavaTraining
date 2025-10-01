package datpv.example.backup;

import datpv.example.backup.core.*;
import datpv.example.backup.storage.LocalStorageProvider;
import datpv.example.backup.storage.MockS3Provider;
import datpv.example.backup.sync.SyncEngine;
import datpv.example.backup.sync.SyncOptions;
import datpv.example.backup.sync.SyncResult;

import java.nio.file.Path;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;

public class Main {
    public static void main(String[] args) throws Exception {
        // sample config
        BackupConfig config = new BackupConfig();
        config.setSourceDir(Path.of("data/source"));
        config.setDestinationDir(Path.of("data/backups"));
        config.setParallelism(4);
        config.setEncryptKey("0123456789abcdef".getBytes()); // demo key (16 bytes)
        config.setThrottleBytesPerSecond(0L); // 0 = no throttle

        // storage providers
        LocalStorageProvider localStorage = new LocalStorageProvider(Path.of("data/backups"));
        MockS3Provider mockS3 = new MockS3Provider(Path.of("data/mock-s3"));

        // create job
        BackupJob job = new BackupJob(config, localStorage);
        job.setProgressListener((ProgressListener) new ConsoleProgressListener());

        CompletableFuture<BackupResult> fut = job.execute();

        // start sync demo in background (bidirectional between source and mock cloud)
        SyncEngine sync = new SyncEngine(localStorage, mockS3, config);
        // run a one-off sync:
        SyncResult sr = sync.synchronize(config.getSourceDir(), Path.of("cloud-root"), new SyncOptions());
        System.out.println("Sync result: " + sr);

        // wait for backup to finish
        BackupResult res = fut.join();
        System.out.println("Backup finished: " + res.getStatus() + " message=" + res.getMessage());
    }
}
