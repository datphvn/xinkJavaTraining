
public interface InventoryObserver {
    void onLowStock(PhysicalProduct product, int currentStock);
}
