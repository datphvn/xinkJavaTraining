import java.util.*;

public class Course {
    private final String courseCode;
    private String courseName;
    private int credits;
    private String instructor;
    private int maxCapacity;
    private final List<Student> enrolledStudents = new ArrayList<>();
    private final Queue<Student> waitlist = new LinkedList<>();
    private Map<String, Integer> schedule = new HashMap<>();

    public Course(String courseCode, String courseName, int credits, int maxCapacity) {
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.credits = credits;
        this.maxCapacity = maxCapacity;
    }

    public boolean enroll(Student s) {
        if (enrolledStudents.size() < maxCapacity) {
            enrolledStudents.add(s);
            return true;
        } else {
            waitlist.add(s);
            return false;
        }
    }

    public void drop(Student s) {
        if (enrolledStudents.remove(s) && !waitlist.isEmpty()) {
            enrolledStudents.add(waitlist.poll()); // pull from waitlist
        }
    }

    public int calculateCourseLoad() {
        return credits * enrolledStudents.size();
    }

    // Getters
    public String getCourseCode() { return courseCode; }
    public String getCourseName() { return courseName; }
    public int getCredits() { return credits; }
}
