package routes;

public class DefaultRoutePlanner implements RoutePlanner {
    @Override
    public Route calculateRoute(String start, String end) {
        System.out.println("Calculating default route from " + start + " to " + end);
        return new Route(start, end);
    }
}
