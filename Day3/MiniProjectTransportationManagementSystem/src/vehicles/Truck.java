package vehicles;

public class Truck extends Vehicle {
    public Truck(String id) {
        super(id, "Truck");
    }

    @Override
    public void drive() {
        System.out.println("Truck " + id + " is hauling cargo.");
    }
}
