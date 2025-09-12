

public class EmailAlertService implements InventoryObserver {
    @Override
    public void onLowStock(PhysicalProduct product, int currentStock) {
        System.out.println("[EMAIL ALERT] Product " + product.getName() + " low stock: " + currentStock);
    }
}
