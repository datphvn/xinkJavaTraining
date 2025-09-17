package collections;

import java.util.*;

public class BoundedQueue<E> implements Queue<E> {
    private final E[] elements;
    private int head = 0, tail = 0, size = 0;
    private final int capacity;

    @SuppressWarnings("unchecked")
    public BoundedQueue(int capacity) {
        this.capacity = capacity;
        this.elements = (E[]) new Object[capacity];
    }

    @Override
    public boolean offer(E e) {
        if (size == capacity) return false;
        elements[tail] = e;
        tail = (tail + 1) % capacity;
        size++;
        return true;
    }

    @Override
    public E poll() {
        if (size == 0) return null;
        E e = elements[head];
        elements[head] = null;
        head = (head + 1) % capacity;
        size--;
        return e;
    }

    @Override
    public E peek() {
        return size == 0 ? null : elements[head];
    }

    @Override public int size() { return size; }
    @Override public boolean isEmpty() { return size == 0; }
    @Override public boolean contains(Object o) {
        for (E e : elements) if (Objects.equals(e, o)) return true;
        return false;
    }
    @Override public Iterator<E> iterator() {
        return new Iterator<>() {
            int idx = 0;
            public boolean hasNext() { return idx < size; }
            public E next() {
                E e = elements[(head + idx) % capacity]; idx++; return e;
            }
        };
    }
    @Override public Object[] toArray() { return Arrays.copyOf(elements, size); }
    @Override public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }
    @Override public boolean add(E e) { if (offer(e)) return true; else throw new IllegalStateException("Full"); }
    @Override public boolean remove(Object o) { throw new UnsupportedOperationException(); }
    @Override public boolean containsAll(Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public boolean addAll(Collection<? extends E> c) { throw new UnsupportedOperationException(); }
    @Override public boolean removeAll(Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public boolean retainAll(Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public void clear() { head = tail = size = 0; Arrays.fill(elements,null); }
    @Override public E remove() { E e = poll(); if (e==null) throw new NoSuchElementException(); return e; }
    @Override public E element() { E e = peek(); if (e==null) throw new NoSuchElementException(); return e; }
}
