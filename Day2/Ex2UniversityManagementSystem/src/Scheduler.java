

public class Scheduler {
    public static boolean hasConflict(Course c1, Course c2) {
        return c1.getSchedule().equals(c2.getSchedule());
    }
}
