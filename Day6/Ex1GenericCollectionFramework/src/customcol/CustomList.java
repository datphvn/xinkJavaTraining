// file: customcol/CustomList.java
package customcol;
import java.util.function.*;
public interface CustomList<E> extends Iterable<E> {
    boolean add(E element);
    void add(int index, E element);
    E get(int index);
    E set(int index, E element);
    E remove(int index);
    boolean remove(Object obj);
    int size();
    boolean isEmpty();

    <T> CustomList<T> map(Function<? super E, ? extends T> mapper);
    CustomList<E> filter(java.util.function.Predicate<? super E> predicate);
    <T> T reduce(T identity, java.util.function.BiFunction<T, ? super E, T> accumulator);
}
