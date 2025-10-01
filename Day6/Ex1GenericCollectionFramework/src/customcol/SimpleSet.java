// file: customcol/SimpleSet.java
package customcol;

import java.util.Iterator;
import java.util.Objects;

public class SimpleSet<E> implements CustomSet<E> {
    private final CustomList<E> list = new CustomArrayList<>();

    @Override
    public boolean add(E e) {
        if (contains(e)) return false;
        list.add(e);
        return true;
    }

    @Override
    public boolean remove(Object o) {
        return list.remove(o);
    }

    @Override
    public boolean contains(Object o) {
        for (E e : list) {
            if (Objects.equals(e, o)) return true;
        }
        return false;
    }

    @Override
    public int size() { return list.size(); }

    @Override
    public boolean isEmpty() { return list.isEmpty(); }

    @Override
    public Iterator<E> iterator() { return list.iterator(); }
}
