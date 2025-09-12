
import java.math.BigDecimal;

public class ServiceProduct extends Product {
    private int durationHours; // example dynamic attribute

    public ServiceProduct(String id, String name, BigDecimal basePrice, int durationHours) {
        super(id, name, basePrice);
        this.durationHours = durationHours;
    }

    public int getDurationHours() { return durationHours; }
    public void setDurationHours(int durationHours) { this.durationHours = durationHours; }

    @Override
    public String getProductType() { return "Service"; }

    @Override
    public boolean isAvailable() { return true; } // limited by scheduling in real system
}
