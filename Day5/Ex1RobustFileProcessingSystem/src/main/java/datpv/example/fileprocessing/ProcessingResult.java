package datpv.example.fileprocessing;

public class ProcessingResult {
    private final boolean success;
    private final String message;

    public ProcessingResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }

    @Override
    public String toString() {
        return "ProcessingResult{" +
                "success=" + success +
                ", message='" + message + '\'' +
                '}';
    }
}
