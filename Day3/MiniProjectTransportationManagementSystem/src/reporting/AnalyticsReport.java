package reporting;

import vehicles.Vehicle;

public class AnalyticsReport implements ReportVisitor {
    @Override
    public void visit(Vehicle vehicle) {
        System.out.println("Reporting on vehicle " + vehicle.getId() + " of type " + vehicle.getType());
    }
}
