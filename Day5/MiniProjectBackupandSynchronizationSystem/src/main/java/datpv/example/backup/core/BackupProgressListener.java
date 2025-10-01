package datpv.example.backup.core;

public interface BackupProgressListener {
    void onProgress(long processedBytes, long totalBytes);
    void onCompleted();
    void onError(Exception e);
}
