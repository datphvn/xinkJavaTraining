import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class StudentManagementSystem implements Serializable {
    private static final long serialVersionUID = 1L;

    private final List<Student> students = new ArrayList<>();
    private final List<Course> courses = new ArrayList<>();
    // prerequisites: map courseCode -> list of prerequisite courseCodes
    private final Map<String, List<String>> prerequisites = new HashMap<>();

    public void addStudent(Student s) { students.add(s); }
    public void addCourse(Course c) { courses.add(c); }

    public void addPrerequisite(String courseCode, String prereqCourseCode) {
        prerequisites.computeIfAbsent(courseCode, k -> new ArrayList<>()).add(prereqCourseCode);
    }

    // Improved registerCourse: checks prerequisites & schedule conflicts & waitlist handling
    public boolean registerCourse(Student s, Course c) {
        // check student exists
        if (!students.contains(s)) return false;
        // check prerequisites
        List<String> pres = prerequisites.getOrDefault(c.getCourseCode(), Collections.emptyList());
        for (String preCode : pres) {
            if (!s.hasCompletedCourse(preCode)) {
                System.out.println("Cannot register: missing prerequisite " + preCode);
                return false;
            }
        }
        // check schedule conflict with student's ACTIVE enrollments
        for (Enrollment en : s.getEnrollments()) {
            if (en.getStatus() == EnrollmentStatus.ACTIVE) {
                Course other = en.getCourse();
                if (other.conflictsWith(c)) {
                    System.out.println("Cannot register: schedule conflict with " + other.getCourseCode());
                    return false;
                }
            }
        }

        // create enrollment
        Enrollment e = new Enrollment(s, c);

        boolean enrolled = c.enroll(s);
        if (enrolled) {
            e.setStatus(EnrollmentStatus.ACTIVE);
            s.addEnrollment(e);
            System.out.println("Registered " + s.getStudentId() + " to " + c.getCourseCode());
            return true;
        } else {
            e.setStatus(EnrollmentStatus.WAITLISTED);
            s.addEnrollment(e);
            System.out.println("Course full - " + s.getStudentId() + " added to waitlist for " + c.getCourseCode());
            return false;
        }
    }

    // assign grade and mark enrollment COMPLETED
    public boolean assignGrade(String studentId, String courseCode, double grade) {
        Student s = findStudentById(studentId);
        if (s == null) return false;
        for (Enrollment e : s.getEnrollments()) {
            if (e.getCourse().getCourseCode().equals(courseCode)) {
                if (e.getStatus() == EnrollmentStatus.WAITLISTED) {
                    System.out.println("Cannot assign grade: student is waitlisted for course");
                    return false;
                }
                e.setGrade(grade);
                e.setStatus(EnrollmentStatus.COMPLETED);
                System.out.println("Assigned grade " + grade + " to " + studentId + " for " + courseCode);
                return true;
            }
        }
        return false;
    }

    public List<Student> searchStudentByName(String name) {
        String k = name.toLowerCase();
        return students.stream()
                .filter(s -> (s.getFirstName() + " " + s.getLastName()).toLowerCase().contains(k))
                .collect(Collectors.toList());
    }

    public Student findStudentById(String id) {
        for (Student s : students) if (s.getStudentId().equals(id)) return s;
        return null;
    }

    public Course findCourseByCode(String code) {
        for (Course c : courses) if (c.getCourseCode().equals(code)) return c;
        return null;
    }

    public void listStudents() {
        System.out.println("Students:");
        students.forEach(s -> System.out.println(" - " + s));
    }

    public void listCourses() {
        System.out.println("Courses:");
        courses.forEach(c -> System.out.println(" - " + c));
    }

    // Persistence: save the whole SMS object
    public void saveData(String filePath) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filePath))) {
            out.writeObject(this);
        }
    }

    public static StudentManagementSystem loadData(String filePath) throws IOException, ClassNotFoundException {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filePath))) {
            return (StudentManagementSystem) in.readObject();
        }
    }

    // Auto test helper used by Main auto-run
    public void populateSampleData() {
        // create sample students and courses as in your original main
        Student s1 = new Student("S001", "Nguyen", "An", java.time.LocalDate.of(2002, 5, 20));
        s1.setEmail("an.nguyen@example.com");
        Student s2 = new Student("S002", "Tran", "Binh", java.time.LocalDate.of(2001, 8, 15));
        s2.setEmail("binh.tran@example.com");
        addStudent(s1);
        addStudent(s2);

        Course c1 = new Course("C101", "Java Programming", 3, 2);
        Course c2 = new Course("C102", "Database Systems", 4, 1);
        addCourse(c1);
        addCourse(c2);

        // example prerequisite (C102 requires C101)
        addPrerequisite("C102", "C101");
    }
}
