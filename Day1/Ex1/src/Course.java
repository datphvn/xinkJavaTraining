import java.io.Serializable;
import java.util.*;

public class Course implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String courseCode;
    private String courseName;
    private int credits;
    private String instructor;
    private int maxCapacity;
    private final List<Student> enrolledStudents = new ArrayList<>();
    private final Queue<Student> waitlist = new LinkedList<>();
    private Map<String, Integer> schedule = new HashMap<>(); // Day -> Hour

    public Course(String courseCode, String courseName, int credits, int maxCapacity) {
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.credits = credits;
        this.maxCapacity = maxCapacity;
    }

    // returns true if added to enrolledStudents; false => added to waitlist
    public boolean enroll(Student s) {
        if (enrolledStudents.contains(s) || waitlist.contains(s)) return false; // already registered
        if (enrolledStudents.size() < maxCapacity) {
            enrolledStudents.add(s);
            return true;
        } else {
            waitlist.add(s);
            return false;
        }
    }

    // drop s; if someone from waitlist promoted, return that Student; else null
    public Student drop(Student s) {
        boolean removed = enrolledStudents.remove(s);
        Student promoted = null;
        if (removed && !waitlist.isEmpty()) {
            promoted = waitlist.poll();
            enrolledStudents.add(promoted);
        } else {
            // if s was in waitlist, remove it
            waitlist.remove(s);
        }
        return promoted;
    }

    public int calculateCourseLoad() {
        return credits * enrolledStudents.size();
    }

    // schedule management
    public void addSchedule(String day, int hour) { schedule.put(day, hour); }
    public Map<String,Integer> getSchedule() { return schedule; }

    // conflict: any day with same hour
    public boolean conflictsWith(Course other) {
        for (Map.Entry<String,Integer> e : schedule.entrySet()) {
            Integer h = other.schedule.get(e.getKey());
            if (h != null && h.equals(e.getValue())) return true;
        }
        return false;
    }

    // Getters
    public String getCourseCode() { return courseCode; }
    public String getCourseName() { return courseName; }
    public int getCredits() { return credits; }
    public List<Student> getEnrolledStudents() { return enrolledStudents; }
    public Queue<Student> getWaitlist() { return waitlist; }

    @Override
    public String toString() {
        return String.format("%s - %s (%d credits) cap=%d enrolled=%d waitlist=%d",
                courseCode, courseName, credits, maxCapacity, enrolledStudents.size(), waitlist.size());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Course)) return false;
        return courseCode.equals(((Course)o).courseCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(courseCode);
    }
}
