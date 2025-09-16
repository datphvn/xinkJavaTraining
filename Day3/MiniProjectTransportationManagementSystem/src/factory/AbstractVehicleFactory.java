package factory;

import vehicles.Vehicle;

public abstract class AbstractVehicleFactory {
    public abstract Vehicle createVehicle(String type);
}
