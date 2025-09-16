package app;

import vehicles.*;
import routes.*;
import drivers.*;
import booking.*;
import tracking.*;
import maintenance.*;
import pricing.*;
import factory.*;
import reporting.*;

public class Main {
    public static void main(String[] args) {
        // Abstract factory
        AbstractVehicleFactory factory = new VehicleFactory();
        Vehicle car = factory.createVehicle("Car");
        Vehicle bus = factory.createVehicle("Bus");

        // Route planning
        RoutePlanner routePlanner = new DefaultRoutePlanner();
        Route route = routePlanner.calculateRoute("A", "B");

        // Driver scheduling
        DriverScheduler scheduler = new DriverScheduler();
        Driver driver = new Driver("John Doe", "D123");
        scheduler.assignDriver(driver, car);

        // Booking
        BookingSystem bookingSystem = new BookingSystem();
        bookingSystem.bookRide("Alice", car, route);

        // Tracking (observer)
        TrackingSystem trackingSystem = new TrackingSystem();
        Tracker tracker = new Tracker("Central Control");
        trackingSystem.addObserver(tracker);
        trackingSystem.updateLocation(car, "Highway 101");

        // Maintenance (template method)
        MaintenanceScheduler maintenanceScheduler = new MaintenanceScheduler();
        maintenanceScheduler.schedule(car);

        // Pricing (strategy)
        PricingStrategy strategy = new PremiumPricingStrategy();
        double price = strategy.calculatePrice(route, car);
        System.out.println("Calculated price: " + price);

        // Reporting (visitor)
        ReportVisitor reportVisitor = new AnalyticsReport();
        car.accept(reportVisitor);
        bus.accept(reportVisitor);
    }
}
