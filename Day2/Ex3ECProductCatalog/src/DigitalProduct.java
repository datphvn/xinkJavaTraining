
import java.math.BigDecimal;

public class DigitalProduct extends Product {
    private String downloadUrl;

    public DigitalProduct(String id, String name, BigDecimal basePrice, String downloadUrl) {
        super(id, name, basePrice);
        this.downloadUrl = downloadUrl;
    }

    public String getDownloadUrl() { return downloadUrl; }
    public void setDownloadUrl(String downloadUrl) { this.downloadUrl = downloadUrl; }

    @Override
    public String getProductType() { return "Digital"; }

    @Override
    public boolean isAvailable() { return true; } // unlimited
}
