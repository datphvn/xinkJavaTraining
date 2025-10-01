// file: customcol/CustomSet.java
package customcol;
public interface CustomSet<E> extends Iterable<E> {
    boolean add(E e);
    boolean remove(Object o);
    boolean contains(Object o);
    int size();
    boolean isEmpty();
}
