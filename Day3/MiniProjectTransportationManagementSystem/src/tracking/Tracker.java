package tracking;

public class Tracker {
    private String name;

    public Tracker(String name) {
        this.name = name;
    }

    public void update(String vehicleId, String location) {
        System.out.println("Tracker " + name + ": Vehicle " + vehicleId + " at " + location);
    }
}
