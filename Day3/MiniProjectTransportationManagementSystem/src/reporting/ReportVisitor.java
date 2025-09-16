package reporting;

import vehicles.Vehicle;

public interface ReportVisitor {
    void visit(Vehicle vehicle);
}
