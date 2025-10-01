// file: customcol/CollectionUtils.java
package customcol;

import java.util.Comparator;

@SuppressWarnings("unchecked")
public class CollectionUtils {

    // sort using merge sort (works for large lists)
    public static <T extends Comparable<? super T>> void sort(CustomList<T> list) {
        sort(list, Comparator.naturalOrder());
    }

    public static <T> void sort(CustomList<T> list, Comparator<? super T> comparator) {
        int n = list.size();
        if (n <= 1) return;
        Object[] arr = new Object[n];
        for (int i = 0; i < n; i++) arr[i] = list.get(i);
        Object[] aux = new Object[n];
        mergeSort(arr, aux, 0, n - 1, (Comparator<Object>) comparator);
        for (int i = 0; i < n; i++) list.set(i, (T) arr[i]);
    }

    private static void mergeSort(Object[] arr, Object[] aux, int lo, int hi, Comparator<Object> cmp) {
        if (lo >= hi) return;
        int mid = (lo + hi) >>> 1;
        mergeSort(arr, aux, lo, mid, cmp);
        mergeSort(arr, aux, mid + 1, hi, cmp);
        int i = lo, j = mid + 1, k = lo;
        while (i <= mid && j <= hi) {
            if (cmp.compare(arr[i], arr[j]) <= 0) aux[k++] = arr[i++];
            else aux[k++] = arr[j++];
        }
        while (i <= mid) aux[k++] = arr[i++];
        while (j <= hi) aux[k++] = arr[j++];
        for (k = lo; k <= hi; k++) arr[k] = aux[k];
    }

    // copy with PECS: src produces ? extends T, dest consumes ? super T
    public static <T> void copy(CustomList<? extends T> src, CustomList<? super T> dest) {
        for (int i = 0; i < src.size(); i++) {
            dest.add(src.get(i)); // compiler accepts because of method generics
        }
    }

    // union (unique) -- preserves first occurrence order
    public static <T> CustomList<T> union(CustomList<? extends T> list1, CustomList<? extends T> list2) {
        CustomList<T> result = new CustomArrayList<>();
        SimpleSet<T> seen = new SimpleSet<>();
        for (int i = 0; i < list1.size(); i++) {
            T e = (T) list1.get(i);
            if (seen.add(e)) result.add(e);
        }
        for (int i = 0; i < list2.size(); i++) {
            T e = (T) list2.get(i);
            if (seen.add(e)) result.add(e);
        }
        return result;
    }
}
