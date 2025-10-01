package datpv.example.backup.core;

public class ConsoleProgressListener implements BackupProgressListener {
    @Override
    public void onProgress(long processedBytes, long totalBytes) {
        double percent = totalBytes > 0 ? (processedBytes * 100.0 / totalBytes) : 0.0;
        System.out.printf("Progress: %.2f%% (%d/%d bytes)%n", percent, processedBytes, totalBytes);
    }

    @Override
    public void onCompleted() {
        System.out.println("Backup completed successfully!");
    }

    @Override
    public void onError(Exception e) {
        System.err.println("Backup error: " + e.getMessage());
    }
}
