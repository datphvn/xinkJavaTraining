// file: customcol/CustomArrayList.java
package customcol;

import java.util.*;
import java.util.function.*;

@SuppressWarnings("unchecked")
public class CustomArrayList<E> implements CustomList<E> {
    private Object[] elements;
    private int size;
    private static final int DEFAULT_CAPACITY = 10;

    public CustomArrayList() {
        this.elements = new Object[DEFAULT_CAPACITY];
        this.size = 0;
    }

    private void ensureCapacity() {
        if (size >= elements.length) {
            int newCap = elements.length * 2;
            elements = Arrays.copyOf(elements, newCap);
        }
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException("" + index);
    }

    private void checkIndexForAdd(int index) {
        if (index < 0 || index > size) throw new IndexOutOfBoundsException("" + index);
    }

    @Override
    public boolean add(E element) {
        ensureCapacity();
        elements[size++] = element;
        return true;
    }

    @Override
    public void add(int index, E element) {
        checkIndexForAdd(index);
        ensureCapacity();
        System.arraycopy(elements, index, elements, index + 1, size - index);
        elements[index] = element;
        size++;
    }

    @Override
    public E get(int index) {
        checkIndex(index);
        return (E) elements[index];
    }

    @Override
    public E set(int index, E element) {
        checkIndex(index);
        E old = (E) elements[index];
        elements[index] = element;
        return old;
    }

    @Override
    public E remove(int index) {
        checkIndex(index);
        E old = (E) elements[index];
        int moved = size - index - 1;
        if (moved > 0) System.arraycopy(elements, index + 1, elements, index, moved);
        elements[--size] = null;
        return old;
    }

    @Override
    public boolean remove(Object obj) {
        for (int i = 0; i < size; i++) {
            if (Objects.equals(elements[i], obj)) {
                remove(i);
                return true;
            }
        }
        return false;
    }

    @Override
    public int size() { return size; }

    @Override
    public boolean isEmpty() { return size == 0; }

    // Functional methods
    @Override
    public <T> CustomList<T> map(Function<? super E, ? extends T> mapper) {
        CustomArrayList<T> out = new CustomArrayList<>();
        for (int i = 0; i < size; i++) {
            out.add(mapper.apply((E) elements[i]));
        }
        return out;
    }

    @Override
    public CustomList<E> filter(java.util.function.Predicate<? super E> predicate) {
        CustomArrayList<E> out = new CustomArrayList<>();
        for (int i = 0; i < size; i++) {
            E e = (E) elements[i];
            if (predicate.test(e)) out.add(e);
        }
        return out;
    }

    @Override
    public <T> T reduce(T identity, BiFunction<T, ? super E, T> accumulator) {
        T acc = identity;
        for (int i = 0; i < size; i++) {
            acc = accumulator.apply(acc, (E) elements[i]);
        }
        return acc;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            int cursor = 0;
            @Override public boolean hasNext() { return cursor < size; }
            @Override public E next() {
                if (!hasNext()) throw new NoSuchElementException();
                return (E) elements[cursor++];
            }
        };
    }

    // helper: toString
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            if (i > 0) sb.append(", ");
            sb.append(elements[i]);
        }
        sb.append("]");
        return sb.toString();
    }
}
