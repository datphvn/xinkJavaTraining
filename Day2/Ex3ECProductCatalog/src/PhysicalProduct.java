
import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicInteger;

public class PhysicalProduct extends Product {
    private final AtomicInteger stock = new AtomicInteger(0); // thread-safe counter

    public PhysicalProduct(String id, String name, BigDecimal basePrice, int initialStock) {
        super(id, name, basePrice);
        this.stock.set(Math.max(0, initialStock));
    }

    public int getStock() { return stock.get(); }

    public void setStock(int qty) { stock.set(Math.max(0, qty)); }

    public boolean decreaseStock(int qty) {
        if (qty <= 0) return false;
        while (true) {
            int current = stock.get();
            if (current < qty) return false;
            if (stock.compareAndSet(current, current - qty)) return true;
        }
    }

    public void increaseStock(int qty) {
        if (qty <= 0) return;
        stock.addAndGet(qty);
    }

    @Override
    public String getProductType() { return "Physical"; }

    @Override
    public boolean isAvailable() { return stock.get() > 0; }
}
