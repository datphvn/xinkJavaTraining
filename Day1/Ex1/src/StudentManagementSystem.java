import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudentManagementSystem {
    private final List<Student> students = new ArrayList<>();
    private final List<Course> courses = new ArrayList<>();
    private final Map<String, List<Course>> prerequisites = new HashMap<>();

    public void addStudent(Student s) {
        students.add(s);
    }

    public void addCourse(Course c) {
        courses.add(c);
    }

    public boolean registerCourse(Student s, Course c) {
        // check prerequisites
        if (prerequisites.containsKey(c.getCourseCode())) {
            for (Course pre : prerequisites.get(c.getCourseCode())) {
                boolean completed = s.calculateGPA() > 0; // mock check
                if (!completed) return false;
            }
        }
        Enrollment e = new Enrollment(s, c);
        if (c.enroll(s)) {
            s.addEnrollment(e);
            return true;
        }
        return false;
    }

    public List<Student> searchStudentByName(String name) {
        return students.stream()
                .filter(s -> (s.getFirstName() + " " + s.getLastName()).contains(name))
                .toList();
    }

    // Data persistence
    public void saveData(String filePath) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filePath))) {
            out.writeObject(students);
            out.writeObject(courses);
        }
    }

    @SuppressWarnings("unchecked")
    public void loadData(String filePath) throws IOException, ClassNotFoundException {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filePath))) {
            students.addAll((List<Student>) in.readObject());
            courses.addAll((List<Course>) in.readObject());
        }
    }
}
