package datpv.example.backup.core;

public class BackupResult {
    private final BackupStatus status;
    private final String message;

    public BackupResult(BackupStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public BackupStatus getStatus() { return status; }
    public String getMessage() { return message; }
}
