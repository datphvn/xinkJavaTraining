
import java.util.ArrayList;
import java.util.List;

public class Student extends Person {
    private final List<Enrollment> enrollments;
    private double gpa;

    public Student(String id, String name, String email) {
        super(id, name, email);
        this.enrollments = new ArrayList<>();
        this.gpa = 0.0;
    }

    public void addEnrollment(Enrollment enrollment) {
        enrollments.add(enrollment);
        recalculateGPA();
    }

    public List<Enrollment> getEnrollments() { return enrollments; }
    public double getGpa() { return gpa; }

    private void recalculateGPA() {
        double totalPoints = 0;
        int totalCredits = 0;
        for (Enrollment e : enrollments) {
            if (e.getGrade() != null) {
                totalPoints += e.getGrade().getPoints() * e.getCourse().getCredits();
                totalCredits += e.getCourse().getCredits();
            }
        }
        this.gpa = totalCredits > 0 ? totalPoints / totalCredits : 0.0;
    }

    public String getAcademicStanding() {
        if (gpa >= 3.7) return "Dean's List";
        else if (gpa >= 2.0) return "Good Standing";
        else return "Probation";
    }

    @Override
    public String getRole() {
        return "Student";
    }
}
