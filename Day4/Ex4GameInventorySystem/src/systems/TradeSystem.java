package systems;

import inventory.Inventory;
import items.Item;

public class TradeSystem {
    public void trade(Inventory inv, Item item, int quantity) {
        if (inv.removeItem(item, quantity)) {
            System.out.println("Traded " + quantity + " of " + item.getName());
        } else {
            System.out.println("Trade failed: not enough " + item.getName());
        }
    }
}
