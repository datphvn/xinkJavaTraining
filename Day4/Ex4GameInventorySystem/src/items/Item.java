package items;

public class Item {
    private final String name;
    private final ItemCategory category;
    private final Rarity rarity;
    private final boolean stackable;
    private final double weight;
    private final EquipmentSlot slot; // null nếu không trang bị được

    public Item(String name, ItemCategory category, Rarity rarity, boolean stackable, double weight, EquipmentSlot slot) {
        this.name = name;
        this.category = category;
        this.rarity = rarity;
        this.stackable = stackable;
        this.weight = weight;
        this.slot = slot;
    }

    public String getName() { return name; }
    public ItemCategory getCategory() { return category; }
    public Rarity getRarity() { return rarity; }
    public boolean isStackable() { return stackable; }
    public double getWeight() { return weight; }
    public EquipmentSlot getSlot() { return slot; }

    @Override
    public String toString() {
        return name + " (" + rarity + ", " + category + ", " +
                (stackable ? "Stackable" : "Unique") + ", " + weight + "kg)";
    }
}
