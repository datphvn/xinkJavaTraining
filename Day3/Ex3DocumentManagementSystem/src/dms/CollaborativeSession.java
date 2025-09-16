package dms;

import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Simplified collaborative editing:
 * - single writer lock
 * - operations recorded
 */
public class CollaborativeSession {
    private final Document doc;
    private final ReentrantLock lock = new ReentrantLock();
    private final List<String> history = new ArrayList<>();

    public CollaborativeSession(Document doc) {
        this.doc = doc;
    }

    public boolean tryEdit(User user, String newContent) {
        if (!user.hasPermission(doc, Permission.WRITE)) throw new SecurityException("No write permission");
        boolean got = lock.tryLock();
        if (!got) return false;
        try {
            String old = doc.getContent();
            doc.write(newContent, user);
            history.add(String.format("%s edited at %s", user.getUsername(), new Date()));
            return true;
        } finally {
            lock.unlock();
        }
    }

    public List<String> getHistory() { return Collections.unmodifiableList(history); }
}
