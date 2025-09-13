import java.io.Serializable;
import java.time.LocalDate;

public class Enrollment implements Serializable {
    private static final long serialVersionUID = 1L;

    private final Student student;
    private final Course course;
    private final LocalDate enrollmentDate;
    private double grade;
    private int attendancePercentage;
    private EnrollmentStatus status;

    public Enrollment(Student student, Course course) {
        this.student = student;
        this.course = course;
        this.enrollmentDate = LocalDate.now();
        this.status = EnrollmentStatus.ACTIVE;
        this.grade = -1; // -1 means not graded yet
    }

    public void recordAttendance(int attended, int total) {
        this.attendancePercentage = (int)((attended * 100.0) / total);
    }

    public double calculateFinalGrade(double examWeight, double attendanceWeight) {
        return (grade * examWeight) + (attendancePercentage * attendanceWeight);
    }

    public void withdraw() {
        this.status = EnrollmentStatus.WITHDRAWN;
    }

    // Getters / Setters
    public double getGrade() { return grade; }
    public void setGrade(double grade) {
        this.grade = grade;
    }
    public Course getCourse() { return course; }
    public Student getStudent() { return student; }
    public EnrollmentStatus getStatus() { return status; }
    public void setStatus(EnrollmentStatus status) { this.status = status; }
    public LocalDate getEnrollmentDate() { return enrollmentDate; }
    public int getAttendancePercentage() { return attendancePercentage; }

    @Override
    public String toString() {
        return String.format("Enrollment[%s->%s, status=%s, grade=%s]",
                student.getStudentId(), course.getCourseCode(), status,
                grade >= 0 ? String.valueOf(grade) : "N/A");
    }
}
