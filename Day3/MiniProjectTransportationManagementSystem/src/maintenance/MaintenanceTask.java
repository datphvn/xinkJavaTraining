package maintenance;

import vehicles.Vehicle;

public abstract class MaintenanceTask {
    public final void perform(Vehicle vehicle) {
        inspect(vehicle);
        repair(vehicle);
        test(vehicle);
    }

    protected abstract void inspect(Vehicle vehicle);
    protected abstract void repair(Vehicle vehicle);

    private void test(Vehicle vehicle) {
        System.out.println("Testing vehicle " + vehicle.getId() + " after maintenance.");
    }
}
