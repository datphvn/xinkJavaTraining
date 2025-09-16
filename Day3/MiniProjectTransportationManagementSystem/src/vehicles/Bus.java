package vehicles;

public class Bus extends Vehicle {
    public Bus(String id) {
        super(id, "Bus");
    }

    @Override
    public void drive() {
        System.out.println("Bus " + id + " is transporting passengers.");
    }
}
