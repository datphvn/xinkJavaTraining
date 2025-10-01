package datpv.example.backup.core;

import java.nio.file.Path;

public class BackupConfig {
    private Path sourceDir;
    private Path destinationDir;
    private int parallelism = 4;
    private byte[] encryptKey; // AES-128/256 key
    private long throttleBytesPerSecond = 0L; // 0 = no throttle
    private boolean compress = true;
    private boolean deduplicate = true;

    public Path getSourceDir() { return sourceDir; }
    public void setSourceDir(Path sourceDir) { this.sourceDir = sourceDir; }

    public Path getDestinationDir() { return destinationDir; }
    public void setDestinationDir(Path destinationDir) { this.destinationDir = destinationDir; }

    public int getParallelism() { return parallelism; }
    public void setParallelism(int parallelism) { this.parallelism = parallelism; }

    public byte[] getEncryptKey() { return encryptKey; }
    public void setEncryptKey(byte[] encryptKey) { this.encryptKey = encryptKey; }

    public long getThrottleBytesPerSecond() { return throttleBytesPerSecond; }
    public void setThrottleBytesPerSecond(long throttleBytesPerSecond) { this.throttleBytesPerSecond = throttleBytesPerSecond; }

    public boolean isCompress() { return compress; }
    public void setCompress(boolean compress) { this.compress = compress; }

    public boolean isDeduplicate() { return deduplicate; }
    public void setDeduplicate(boolean deduplicate) { this.deduplicate = deduplicate; }
}
