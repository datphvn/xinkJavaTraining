package systems;

import items.*;

public class CraftingSystem {
    public Item craft(String name, ItemCategory category, Rarity rarity, double weight, EquipmentSlot slot) {
        System.out.println("Crafted a new " + name);
        return new Item(name, category, rarity, false, weight, slot);
    }
}
