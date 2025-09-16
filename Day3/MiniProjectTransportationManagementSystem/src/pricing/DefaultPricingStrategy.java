package pricing;

import routes.Route;
import vehicles.Vehicle;

public class DefaultPricingStrategy implements PricingStrategy {
    @Override
    public double calculatePrice(Route route, Vehicle vehicle) {
        return 50.0;
    }
}
