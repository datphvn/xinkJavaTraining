import java.time.LocalDate;

public class Enrollment {
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

    // Getters
    public double getGrade() { return grade; }
    public void setGrade(double grade) { this.grade = grade; }
    public Course getCourse() { return course; }
}
