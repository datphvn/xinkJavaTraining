// file: customcol/SynchronizedCustomList.java
package customcol;

import java.util.Iterator;
import java.util.function.BiFunction;
import java.util.function.Function;

public class SynchronizedCustomList<E> implements CustomList<E> {
    private final CustomList<E> delegate;
    private final Object lock = new Object();

    public SynchronizedCustomList(CustomList<E> delegate) {
        this.delegate = delegate;
    }

    @Override public boolean add(E element) { synchronized(lock){ return delegate.add(element); } }
    @Override public void add(int index, E element) { synchronized(lock){ delegate.add(index, element); } }
    @Override public E get(int index) { synchronized(lock){ return delegate.get(index); } }
    @Override public E set(int index, E element) { synchronized(lock){ return delegate.set(index, element); } }
    @Override public E remove(int index) { synchronized(lock){ return delegate.remove(index); } }
    @Override public boolean remove(Object obj) { synchronized(lock){ return delegate.remove(obj); } }
    @Override public int size() { synchronized(lock){ return delegate.size(); } }
    @Override public boolean isEmpty() { synchronized(lock){ return delegate.isEmpty(); } }

    @Override
    public <T> CustomList<T> map(Function<? super E, ? extends T> mapper) {
        synchronized(lock){ return delegate.map(mapper); }
    }

    @Override
    public CustomList<E> filter(java.util.function.Predicate<? super E> predicate) {
        synchronized(lock){ return delegate.filter(predicate); }
    }

    @Override
    public <T> T reduce(T identity, BiFunction<T, ? super E, T> accumulator) {
        synchronized(lock){ return delegate.reduce(identity, accumulator); }
    }

    @Override
    public Iterator<E> iterator() {
        // Iterator is not thread-safe by default. We return the delegate iterator but document usage.
        return delegate.iterator();
    }
}
