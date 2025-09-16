package vehicles;

public class Bike extends Vehicle {
    public Bike(String id) {
        super(id, "Bike");
    }

    @Override
    public void drive() {
        System.out.println("Bike " + id + " is on the road.");
    }
}
