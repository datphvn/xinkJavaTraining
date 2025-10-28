package com.fplib.collection;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;

// --- 1. Immutable Collection Base (Abstract) ---
public abstract class ImmutableList<T> implements Iterable<T> {

    // Functional operations
    public abstract <R> ImmutableList<R> map(Function<T, R> mapper);
    public abstract ImmutableList<T> filter(Predicate<T> predicate);
    public abstract <R> R fold(R identity, BiFunction<R, T, R> accumulator);
    public abstract ImmutableList<T> take(int n);
    public abstract ImmutableList<T> drop(int n);
    public abstract <R> ImmutableList<R> flatMap(Function<T, Iterable<R>> mapper);

    // List specific access
    public abstract T head();
    public abstract ImmutableList<T> tail();

    // Utility methods
    public abstract int size();
    public abstract boolean isEmpty();
    public abstract boolean contains(T element);
    public abstract T[] toArray(IntFunction<T[]> arrayFactory);

    @SuppressWarnings("unchecked")
    public static <T> ImmutableList<T> empty() {
        return (ImmutableList<T>) Empty.INSTANCE;
    }

    public static <T> ImmutableList<T> of(T... elements) {
        ImmutableList<T> result = empty();
        for (int i = elements.length - 1; i >= 0; i--) {
            result = new Cons<>(elements[i], result);
        }
        return result;
    }
}

// --- 2. Concrete Empty List ---
class Empty<T> extends ImmutableList<T> {
    static final Empty<?> INSTANCE = new Empty<>();

    @Override public T head() { throw new NoSuchElementException("Head on empty list"); }
    @Override public ImmutableList<T> tail() { throw new NoSuchElementException("Tail on empty list"); }
    @Override public int size() { return 0; }
    @Override public boolean isEmpty() { return true; }

    @Override public <R> ImmutableList<R> map(Function<T, R> mapper) { return ImmutableList.empty(); }
    @Override public ImmutableList<T> filter(Predicate<T> predicate) { return this; }
    @Override public <R> R fold(R identity, BiFunction<R, T, R> accumulator) { return identity; }
    @Override public ImmutableList<T> take(int n) { return this; }
    @Override public ImmutableList<T> drop(int n) { return this; }
    @Override public <R> ImmutableList<R> flatMap(Function<T, Iterable<R>> mapper) { return ImmutableList.empty(); }
    @Override public boolean contains(T element) { return false; }
    @Override public T[] toArray(IntFunction<T[]> arrayFactory) { return arrayFactory.apply(0); }
    @Override public Iterator<T> iterator() { return Collections.emptyIterator(); }
    @Override public String toString() { return "[]"; }
}

// --- 3. Concrete Cons (Construct) List ---
class Cons<T> extends ImmutableList<T> {
    private final T head;
    private final ImmutableList<T> tail;
    private final int size;

    Cons(T head, ImmutableList<T> tail) {
        this.head = head;
        this.tail = tail;
        this.size = 1 + tail.size();
    }

    @Override public T head() { return head; }
    @Override public ImmutableList<T> tail() { return tail; }
    @Override public int size() { return size; }
    @Override public boolean isEmpty() { return false; }

    // Implemented recursively/functionally
    @Override
    public <R> ImmutableList<R> map(Function<T, R> mapper) {
        return new Cons<>(mapper.apply(head), tail.map(mapper));
    }

    @Override
    public ImmutableList<T> filter(Predicate<T> predicate) {
        if (predicate.test(head)) {
            return new Cons<>(head, tail.filter(predicate));
        }
        return tail.filter(predicate);
    }

    @Override
    public <R> R fold(R identity, BiFunction<R, T, R> accumulator) {
        // foldLeft
        return tail.fold(accumulator.apply(identity, head), accumulator);
    }

    @Override
    public ImmutableList<T> take(int n) {
        if (n <= 0 || isEmpty()) return ImmutableList.empty();
        return new Cons<>(head, tail.take(n - 1));
    }

    @Override
    public ImmutableList<T> drop(int n) {
        if (n <= 0 || isEmpty()) return this;
        return tail.drop(n - 1);
    }

    @Override
    public <R> ImmutableList<R> flatMap(Function<T, Iterable<R>> mapper) {
        // NOTE: FlatMap is complex in strict recursive lists. We use stream conversion for simplicity.
        // A truly pure FP implementation would involve reversing and fold.
        List<R> list = new ArrayList<>();
        for (T item : this) {
            for (R mappedItem : mapper.apply(item)) {
                list.add(mappedItem);
            }
        }
        return ImmutableList.of((R[]) list.toArray());
    }

    @Override
    public boolean contains(T element) {
        return Objects.equals(head, element) || tail.contains(element);
    }

    @Override
    public T[] toArray(IntFunction<T[]> arrayFactory) {
        T[] array = arrayFactory.apply(size);
        int i = 0;
        for (T item : this) {
            array[i++] = item;
        }
        return array;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private ImmutableList<T> current = Cons.this;

            @Override public boolean hasNext() { return !current.isEmpty(); }
            @Override public T next() {
                if (!hasNext()) throw new NoSuchElementException();
                T value = current.head();
                current = current.tail();
                return value;
            }
        };
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        ImmutableList<T> current = this;
        while (!current.isEmpty()) {
            sb.append(current.head());
            current = current.tail();
            if (!current.isEmpty()) {
                sb.append(", ");
            }
        }
        return sb.append("]").toString();
    }
}
