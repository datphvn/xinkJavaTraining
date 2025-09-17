package collections;

import java.util.*;

public class SkipList<K extends Comparable<K>, V> {
    private static final int MAX_LEVEL = 16;
    private final Random rand = new Random();

    private static class Node<K, V> {
        K key;
        V value;
        Node<K, V>[] forward;

        @SuppressWarnings("unchecked")
        Node(K k, V v, int lvl) {
            key = k;
            value = v;
            forward = (Node<K, V>[]) new Node[lvl + 1]; // ✅ fix
        }
    }

    private final Node<K, V> head = new Node<>(null, null, MAX_LEVEL);
    private int level = 0;

    public void put(K key, V value) {
        Node<K, V>[] update = new Node[MAX_LEVEL + 1];
        Node<K, V> x = head;
        for (int i = level; i >= 0; i--) {
            while (x.forward[i] != null && x.forward[i].key.compareTo(key) < 0) {
                x = x.forward[i];
            }
            update[i] = x;
        }
        x = x.forward[0];
        if (x != null && x.key.equals(key)) {
            x.value = value;
        } else {
            int lvl = randomLevel();
            if (lvl > level) {
                for (int i = level + 1; i <= lvl; i++) {
                    update[i] = head;
                }
                level = lvl;
            }
            Node<K, V> newNode = new Node<>(key, value, lvl);
            for (int i = 0; i <= lvl; i++) {
                newNode.forward[i] = update[i].forward[i];
                update[i].forward[i] = newNode;
            }
        }
    }

    public V get(K key) {
        Node<K, V> x = head;
        for (int i = level; i >= 0; i--) {
            while (x.forward[i] != null && x.forward[i].key.compareTo(key) < 0) {
                x = x.forward[i];
            }
        }
        x = x.forward[0];
        return (x != null && x.key.equals(key)) ? x.value : null;
    }

    private int randomLevel() {
        int lvl = 0;
        while (rand.nextBoolean() && lvl < MAX_LEVEL) {
            lvl++;
        }
        return lvl;
    }
}
