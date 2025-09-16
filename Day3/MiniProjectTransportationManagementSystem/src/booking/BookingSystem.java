package booking;

import vehicles.Vehicle;
import routes.Route;

public class BookingSystem {
    public void bookRide(String customer, Vehicle vehicle, Route route) {
        System.out.println("Customer " + customer + " booked a " + vehicle.getType() +
                " from " + route.getStart() + " to " + route.getEnd());
    }
}
