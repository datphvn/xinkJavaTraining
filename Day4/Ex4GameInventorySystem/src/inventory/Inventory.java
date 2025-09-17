package inventory;

import items.*;
import java.util.*;

public class Inventory {
    private final Map<Item, Integer> items = new HashMap<>();
    private final Map<EquipmentSlot, Item> equipped = new HashMap<>();
    private final int maxSlots;
    private final double maxWeight;

    public Inventory(int maxSlots, double maxWeight) {
        this.maxSlots = maxSlots;
        this.maxWeight = maxWeight;
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            equipped.put(slot, null);
        }
    }

    public double getCurrentWeight() {
        return items.entrySet().stream()
                .mapToDouble(e -> e.getKey().getWeight() * e.getValue())
                .sum();
    }

    public boolean addItem(Item item, int quantity) {
        if (getCurrentWeight() + item.getWeight() * quantity > maxWeight) return false;
        if (!item.isStackable() && quantity > 1) return false;

        items.merge(item, quantity, Integer::sum);
        return true;
    }

    public boolean removeItem(Item item, int quantity) {
        if (!items.containsKey(item)) return false;

        int current = items.get(item);
        if (current < quantity) return false;

        if (current == quantity) items.remove(item);
        else items.put(item, current - quantity);

        return true;
    }

    public void equipItem(Item item) {
        if (item.getSlot() == null) {
            System.out.println("Item cannot be equipped.");
            return;
        }
        equipped.put(item.getSlot(), item);
        System.out.println(item.getName() + " equipped to " + item.getSlot());
    }

    public void printInventory() {
        System.out.println("Inventory:");
        items.forEach((item, qty) -> System.out.println(" - " + item + " x" + qty));
    }
}
