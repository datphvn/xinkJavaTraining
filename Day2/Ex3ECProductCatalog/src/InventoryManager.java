

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class InventoryManager {
    private final List<InventoryObserver> observers = new CopyOnWriteArrayList<>();
    private final int lowStockThreshold;

    public InventoryManager(int lowStockThreshold) {
        this.lowStockThreshold = lowStockThreshold;
    }

    public void registerObserver(InventoryObserver o) { observers.add(o); }
    public void unregisterObserver(InventoryObserver o) { observers.remove(o); }

    public void updateStock(PhysicalProduct product, int newStock) {
        product.setStock(newStock);
        if (newStock <= lowStockThreshold) {
            for (InventoryObserver o : observers) {
                o.onLowStock(product, newStock);
            }
        }
    }

    public void increaseStock(PhysicalProduct product, int qty) {
        product.increaseStock(qty);
        if (product.getStock() <= lowStockThreshold) {
            for (InventoryObserver o : observers) o.onLowStock(product, product.getStock());
        }
    }

    public void decreaseStock(PhysicalProduct product, int qty) {
        boolean ok = product.decreaseStock(qty);
        if (!ok) throw new IllegalStateException("Not enough stock");
        if (product.getStock() <= lowStockThreshold) {
            for (InventoryObserver o : observers) o.onLowStock(product, product.getStock());
        }
    }
}
