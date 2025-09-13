import java.time.Instant;

public class Notification {
    private final String message;
    private final Instant time = Instant.now();

    public Notification(String message) {
        this.message = message;
    }

    public String getMessage() { return message; }
    public Instant getTime() { return time; }

    @Override
    public String toString() {
        return "[" + time + "] " + message;
    }
}
