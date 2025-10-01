package datpv.example.backup.core;

public interface ProgressListener {
    void onFileStart(String relativePath);
    void onFileProgress(String relativePath, long bytesCopied, long totalBytes);
    void onFileComplete(String relativePath, boolean success);
    void onOverallProgress(long filesProcessed, long totalFiles);
}
