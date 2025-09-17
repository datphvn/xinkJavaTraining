import items.*;
import inventory.*;
import systems.*;

public class Main {
    public static void main(String[] args) {
        Inventory inv = new Inventory(20, 50.0);

        Item sword = new Item("Iron Sword", ItemCategory.WEAPON, Rarity.UNCOMMON, false, 5.0, EquipmentSlot.WEAPON);
        Item potion = new Item("Health Potion", ItemCategory.POTION, Rarity.COMMON, true, 0.5, null);
        Item shield = new Item("Wooden Shield", ItemCategory.ARMOR, Rarity.COMMON, false, 3.5, EquipmentSlot.SHIELD);

        inv.addItem(sword, 1);
        inv.addItem(potion, 5);
        inv.addItem(shield, 1);

        inv.printInventory();

        inv.equipItem(sword);
        inv.equipItem(shield);

        // Trade & Craft demo
        TradeSystem trade = new TradeSystem();
        trade.trade(inv, potion, 2);

        CraftingSystem crafting = new CraftingSystem();
        Item crafted = crafting.craft("Steel Sword", ItemCategory.WEAPON, Rarity.RARE, 6.0, EquipmentSlot.WEAPON);
        inv.addItem(crafted, 1);

        inv.printInventory();
    }
}
