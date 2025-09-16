package drivers;

import vehicles.Vehicle;

public class DriverScheduler {
    public void assignDriver(Driver driver, Vehicle vehicle) {
        System.out.println("Assigned driver " + driver.getName() +
                " (License: " + driver.getLicenseId() + ") to " + vehicle.getType());
    }
}
