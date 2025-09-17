package collections;

import java.util.*;

public class CircularBuffer<E> {
    private final E[] buffer;
    private int head = 0, tail = 0, size = 0;

    @SuppressWarnings("unchecked")
    public CircularBuffer(int capacity) {
        buffer = (E[]) new Object[capacity];
    }

    public void add(E e) {
        buffer[tail] = e;
        tail = (tail + 1) % buffer.length;
        if (size == buffer.length) head = (head + 1) % buffer.length;
        else size++;
    }

    public E get() {
        if (size == 0) return null;
        E e = buffer[head];
        head = (head + 1) % buffer.length;
        size--;
        return e;
    }

    public boolean isEmpty() { return size==0; }
    public boolean isFull() { return size==buffer.length; }
}
