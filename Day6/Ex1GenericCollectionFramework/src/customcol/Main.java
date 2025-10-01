// file: customcol/Main.java
package customcol;

public class Main {
    public static void main(String[] args) {
        CustomList<Integer> list = new CustomArrayList<>();
        list.add(5); list.add(3); list.add(8); list.add(1);
        System.out.println("Before: " + list);
        CollectionUtils.sort(list);
        System.out.println("After sort: " + list);

        CustomList<Integer> mapped = list.map(i -> i * 2);
        System.out.println("Mapped *2: " + mapped);

        CustomList<Integer> filtered = list.filter(i -> i % 2 == 1);
        System.out.println("Odd: " + filtered);

        Integer sum = list.reduce(0, (acc, v) -> acc + v);
        System.out.println("Sum: " + sum);

        // copy / union
        CustomList<Number> target = new CustomArrayList<>();
        CollectionUtils.copy(list, target); // copy ints into numbers
        System.out.println("Copied to target<Number>: " + target);

        CustomList<Integer> a = new CustomArrayList<>();
        a.add(1); a.add(2);
        CustomList<Integer> b = new CustomArrayList<>();
        b.add(2); b.add(3);
        CustomList<Integer> u = CollectionUtils.union(a, b);
        System.out.println("Union: " + u);

        // map + set uniqueness
        SimpleSet<String> set = new SimpleSet<>();
        set.add("a"); set.add("b"); set.add("a");
        System.out.println("Set size (should be 2): " + set.size());

        // simple map
        SimpleMap<String, Integer> map = new SimpleMap<>();
        map.put("x", 10); map.put("y", 20);
        System.out.println("map.get(x) = " + map.get("x"));

        // concurrent map adapter
        ConcurrentMapAdapter<String,Integer> cmap = new ConcurrentMapAdapter<>();
        cmap.put("k", 100);
        System.out.println("concurrent map k = " + cmap.get("k"));
    }
}
