package storage;

import java.util.LinkedList;

public class CircularBuffer<T> {
    private final int capacity;
    private final LinkedList<T> buffer = new LinkedList<>();

    public CircularBuffer(int capacity) { this.capacity = capacity; }

    public void add(T item) {
        if (buffer.size() == capacity) buffer.removeFirst();
        buffer.addLast(item);
    }

    public T poll() { return buffer.isEmpty() ? null : buffer.removeFirst(); }
    public T peek() { return buffer.peekFirst(); }
    public boolean isEmpty() { return buffer.isEmpty(); }
}
