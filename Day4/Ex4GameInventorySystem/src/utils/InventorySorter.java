package utils;

import items.*;
import java.util.*;

public class InventorySorter {
    public static List<Item> sortByRarity(Collection<Item> items) {
        List<Item> list = new ArrayList<>(items);
        list.sort(Comparator.comparing(Item::getRarity));
        return list;
    }

    public static List<Item> sortByWeight(Collection<Item> items) {
        List<Item> list = new ArrayList<>(items);
        list.sort(Comparator.comparingDouble(Item::getWeight));
        return list;
    }
}
