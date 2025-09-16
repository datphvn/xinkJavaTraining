package pricing;

import routes.Route;
import vehicles.Vehicle;

public interface PricingStrategy {
    double calculatePrice(Route route, Vehicle vehicle);
}
