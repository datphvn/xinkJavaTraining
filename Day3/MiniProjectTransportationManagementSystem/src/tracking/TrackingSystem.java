package tracking;

import vehicles.Vehicle;
import java.util.ArrayList;
import java.util.List;

public class TrackingSystem {
    private List<Tracker> observers = new ArrayList<>();

    public void addObserver(Tracker tracker) {
        observers.add(tracker);
    }

    public void updateLocation(Vehicle vehicle, String location) {
        for (Tracker tracker : observers) {
            tracker.update(vehicle.getId(), location);
        }
    }
}
