

import java.util.ArrayList;
import java.util.List;

public class Department {
    private final String name;
    private final List<Course> courses;
    private final List<Professor> professors;

    public Department(String name) {
        this.name = name;
        this.courses = new ArrayList<>();
        this.professors = new ArrayList<>();
    }

    public void addCourse(Course c) { courses.add(c); }
    public void addProfessor(Professor p) { professors.add(p); }

    public List<Course> getCourses() { return courses; }
    public List<Professor> getProfessors() { return professors; }
    public String getName() { return name; }
}
