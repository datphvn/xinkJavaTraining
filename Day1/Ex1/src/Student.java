import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Student implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String studentId;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private String email;
    private String phoneNumber;
    private Address address;
    private final List<Enrollment> enrollments = new ArrayList<>();

    public Student(String studentId, String firstName, String lastName, LocalDate birthDate) {
        if (studentId == null || studentId.isBlank())
            throw new IllegalArgumentException("Student ID cannot be empty");
        this.studentId = studentId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
    }

    // Getters/Setters
    public String getStudentId() { return studentId; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public LocalDate getBirthDate() { return birthDate; }
    public String getEmail() { return email; }
    public String getPhoneNumber() { return phoneNumber; }
    public Address getAddress() { return address; }

    public void setEmail(String email) {
        if (email != null && !email.contains("@")) throw new IllegalArgumentException("Invalid email");
        this.email = email;
    }

    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public void setAddress(Address address) { this.address = address; }

    public void addEnrollment(Enrollment e) { enrollments.add(e); }
    public List<Enrollment> getEnrollments() { return enrollments; }

    // Check if student has completed a course (pass threshold >=5.0)
    public boolean hasCompletedCourse(String courseCode) {
        for (Enrollment e : enrollments) {
            if (e.getCourse().getCourseCode().equals(courseCode)
                    && e.getStatus() == EnrollmentStatus.COMPLETED
                    && e.getGrade() >= 5.0) {
                return true;
            }
        }
        return false;
    }

    // GPA: only consider COMPLETED enrollments
    public double calculateGPA() {
        double totalPoints = 0;
        int totalCredits = 0;
        for (Enrollment e : enrollments) {
            if (e.getStatus() == EnrollmentStatus.COMPLETED && e.getGrade() >= 0) {
                totalPoints += e.getGrade() * e.getCourse().getCredits();
                totalCredits += e.getCourse().getCredits();
            }
        }
        return totalCredits == 0 ? 0.0 : totalPoints / totalCredits;
    }

    public String generateTranscript() {
        StringBuilder sb = new StringBuilder();
        sb.append("Transcript for ").append(firstName).append(" ").append(lastName).append("\n");
        for (Enrollment e : enrollments) {
            sb.append(e.getCourse().getCourseCode())
                    .append(" - ").append(e.getCourse().getCourseName())
                    .append(" | status=").append(e.getStatus())
                    .append(" | grade=").append(e.getGrade() >= 0 ? e.getGrade() : "N/A")
                    .append("\n");
        }
        sb.append("GPA: ").append(String.format("%.2f", calculateGPA()));
        return sb.toString();
    }

    public boolean checkGraduationRequirements(int requiredCredits, double minGpa) {
        int earnedCredits = enrollments.stream()
                .filter(e -> e.getStatus() == EnrollmentStatus.COMPLETED && e.getGrade() >= 5.0)
                .mapToInt(e -> e.getCourse().getCredits())
                .sum();
        return earnedCredits >= requiredCredits && calculateGPA() >= minGpa;
    }

    @Override
    public String toString() {
        return String.format("%s: %s %s (email=%s)", studentId, firstName, lastName, email);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Student)) return false;
        return studentId.equals(((Student)o).studentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentId);
    }
}
