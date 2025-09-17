import collections.*;

public class Main {
    public static void main(String[] args) {
        // ==== Test BoundedQueue ====
        System.out.println("=== BoundedQueue Test ===");
        BoundedQueue<Integer> queue = new BoundedQueue<>(3);
        queue.offer(1);
        queue.offer(2);
        queue.offer(3);
        System.out.println("Queue full? offer(4): " + queue.offer(4));
        System.out.println("Peek: " + queue.peek());
        System.out.println("Poll: " + queue.poll());
        System.out.println("Poll: " + queue.poll());
        System.out.println("Poll: " + queue.poll());
        System.out.println("Empty poll: " + queue.poll());

        // ==== Test BiMap ====
        System.out.println("\n=== BiMap Test ===");
        BiMap<String, Integer> biMap = new BiMap<>();
        biMap.put("Alice", 1);
        biMap.put("Bob", 2);
        System.out.println("GetValue(Alice): " + biMap.getValue("Alice"));
        System.out.println("GetKey(2): " + biMap.getKey(2));
        biMap.removeByKey("Alice");
        System.out.println("After removing Alice, getValue(Alice): " + biMap.getValue("Alice"));

        // ==== Test CircularBuffer ====
        System.out.println("\n=== CircularBuffer Test ===");
        CircularBuffer<String> buffer = new CircularBuffer<>(3);
        buffer.add("A");
        buffer.add("B");
        buffer.add("C");
        buffer.add("D"); // overwrite oldest
        System.out.println("Get: " + buffer.get());
        System.out.println("Get: " + buffer.get());
        System.out.println("Get: " + buffer.get());
        System.out.println("Get empty: " + buffer.get());

        // ==== Test LRUCache ====
        System.out.println("\n=== LRUCache Test ===");
        LRUCache<Integer, String> cache = new LRUCache<>(2);
        cache.put(1, "one");
        cache.put(2, "two");
        System.out.println("Get(1): " + cache.get(1));
        cache.put(3, "three"); // evict key=2
        System.out.println("Get(2): " + cache.get(2));
        System.out.println("Cache size: " + cache.size());

        // ==== Test MultiMap ====
        System.out.println("\n=== MultiMap Test ===");
        MultiMap<String, String> multiMap = new MultiMap<>();
        multiMap.put("fruit", "apple");
        multiMap.put("fruit", "banana");
        multiMap.put("fruit", "cherry");
        System.out.println("Values for fruit: " + multiMap.get("fruit"));
        multiMap.remove("fruit", "banana");
        System.out.println("After remove banana: " + multiMap.get("fruit"));

        // ==== Test SkipList ====
        System.out.println("\n=== SkipList Test ===");
        SkipList<Integer, String> skipList = new SkipList<>();
        skipList.put(1, "one");
        skipList.put(3, "three");
        skipList.put(2, "two");
        System.out.println("Get(2): " + skipList.get(2));
        System.out.println("Get(3): " + skipList.get(3));
        System.out.println("Get(4): " + skipList.get(4));

        // ==== Test Trie ====
        System.out.println("\n=== Trie Test ===");
        Trie trie = new Trie();
        trie.insert("hello");
        trie.insert("helium");
        System.out.println("Search 'hello': " + trie.search("hello"));
        System.out.println("Search 'hell': " + trie.search("hell"));
        System.out.println("StartsWith 'hel': " + trie.startsWith("hel"));
    }
}
