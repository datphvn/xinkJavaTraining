package pricing;

import routes.Route;
import vehicles.Vehicle;

public class PremiumPricingStrategy implements PricingStrategy {
    @Override
    public double calculatePrice(Route route, Vehicle vehicle) {
        return 100.0;
    }
}
