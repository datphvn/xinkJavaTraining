import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Student {
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
        if (!email.contains("@")) throw new IllegalArgumentException("Invalid email");
        this.email = email;
    }

    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public void setAddress(Address address) { this.address = address; }

    public void addEnrollment(Enrollment e) {
        enrollments.add(e);
    }


    public List<Enrollment> getEnrollments() {
        return enrollments;
    }

    public double calculateGPA() {
        if (enrollments.isEmpty()) return 0.0;
        double totalPoints = 0;
        int totalCredits = 0;
        for (Enrollment e : enrollments) {
            totalPoints += e.getGrade() * e.getCourse().getCredits();
            totalCredits += e.getCourse().getCredits();
        }
        return totalCredits == 0 ? 0 : totalPoints / totalCredits;
    }

    public String generateTranscript() {
        StringBuilder sb = new StringBuilder();
        sb.append("Transcript for ").append(firstName).append(" ").append(lastName).append("\n");
        for (Enrollment e : enrollments) {
            sb.append(e.getCourse().getCourseCode())
                    .append(" - ").append(e.getCourse().getCourseName())
                    .append(": ").append(e.getGrade()).append("\n");
        }
        sb.append("GPA: ").append(calculateGPA());
        return sb.toString();
    }

    public boolean checkGraduationRequirements(int requiredCredits, double minGpa) {
        int earnedCredits = enrollments.stream()
                .filter(e -> e.getGrade() >= 5.0) // pass threshold
                .mapToInt(e -> e.getCourse().getCredits())
                .sum();
        return earnedCredits >= requiredCredits && calculateGPA() >= minGpa;
    }
}
