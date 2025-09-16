package routes;

public class Route {
    private String start;
    private String end;

    public Route(String start, String end) {
        this.start = start;
        this.end = end;
    }

    public String getStart() { return start; }
    public String getEnd() { return end; }
}
