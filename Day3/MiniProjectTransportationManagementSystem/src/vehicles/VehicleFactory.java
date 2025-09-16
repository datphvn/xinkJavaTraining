package vehicles;

import factory.AbstractVehicleFactory;

public class VehicleFactory extends AbstractVehicleFactory {
    private static int counter = 1;

    @Override
    public Vehicle createVehicle(String type) {
        switch (type) {
            case "Car": return new Car("C" + counter++);
            case "Truck": return new Truck("T" + counter++);
            case "Bus": return new Bus("B" + counter++);
            case "Bike": return new Bike("BK" + counter++);
            default: throw new IllegalArgumentException("Unknown vehicle type: " + type);
        }
    }
}
