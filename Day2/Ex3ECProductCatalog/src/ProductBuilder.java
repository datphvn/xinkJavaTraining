
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class ProductBuilder {
    private String id;
    private String name;
    private String description;
    private BigDecimal basePrice = BigDecimal.ZERO;
    private final Map<String,String> attributes = new HashMap<>();

    public ProductBuilder id(String id) { this.id = id; return this; }
    public ProductBuilder name(String name) { this.name = name; return this; }
    public ProductBuilder description(String description) { this.description = description; return this; }
    public ProductBuilder basePrice(BigDecimal price) { this.basePrice = price; return this; }
    public ProductBuilder addAttribute(String k, String v) { attributes.put(k,v); return this; }

    public PhysicalProduct buildPhysical(int initialStock) {
        PhysicalProduct p = new PhysicalProduct(id, name, basePrice, initialStock);
        p.setDescription(description);
        attributes.forEach(p::addAttribute);
        return p;
    }

    public DigitalProduct buildDigital(String downloadUrl) {
        DigitalProduct p = new DigitalProduct(id, name, basePrice, downloadUrl);
        p.setDescription(description);
        attributes.forEach(p::addAttribute);
        return p;
    }

    public ServiceProduct buildService(int durationHours) {
        ServiceProduct p = new ServiceProduct(id, name, basePrice, durationHours);
        p.setDescription(description);
        attributes.forEach(p::addAttribute);
        return p;
    }
}
