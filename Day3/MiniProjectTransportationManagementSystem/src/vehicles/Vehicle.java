package vehicles;

import reporting.ReportVisitor;

public abstract class Vehicle {
    protected String id;
    protected String type;

    public Vehicle(String id, String type) {
        this.id = id;
        this.type = type;
    }

    public abstract void drive();

    public void accept(ReportVisitor visitor) {
        visitor.visit(this);
    }

    public String getId() { return id; }
    public String getType() { return type; }
}
