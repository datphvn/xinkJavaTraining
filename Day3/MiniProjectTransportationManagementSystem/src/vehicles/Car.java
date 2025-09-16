package vehicles;

public class Car extends Vehicle {
    public Car(String id) {
        super(id, "Car");
    }

    @Override
    public void drive() {
        System.out.println("Car " + id + " is driving.");
    }
}
