import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

public class NotificationService {
    // For demo: subscribers are simple Consumers that accept Notification
    private final CopyOnWriteArrayList<Consumer<Notification>> subscribers = new CopyOnWriteArrayList<>();

    public void subscribe(Consumer<Notification> handler) {
        subscribers.add(handler);
    }

    public void unsubscribe(Consumer<Notification> handler) {
        subscribers.remove(handler);
    }

    public void notifyAllSubscribers(Notification n) {
        for (Consumer<Notification> sub : subscribers) {
            try {
                sub.accept(n);
            } catch (Exception ignored) {}
        }
    }
}
