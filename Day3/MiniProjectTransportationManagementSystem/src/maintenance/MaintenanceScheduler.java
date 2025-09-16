package maintenance;

import vehicles.Vehicle;

public class MaintenanceScheduler extends MaintenanceTask {
    @Override
    protected void inspect(Vehicle vehicle) {
        System.out.println("Inspecting vehicle " + vehicle.getId());
    }

    @Override
    protected void repair(Vehicle vehicle) {
        System.out.println("Repairing vehicle " + vehicle.getId());
    }

    public void schedule(Vehicle vehicle) {
        perform(vehicle);
    }
}
