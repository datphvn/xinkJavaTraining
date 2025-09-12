

import java.util.ArrayList;
import java.util.List;

public class Professor extends Person {
    private final List<Course> coursesTaught;

    public Professor(String id, String name, String email) {
        super(id, name, email);
        this.coursesTaught = new ArrayList<>();
    }

    public void assignCourse(Course c) {
        coursesTaught.add(c);
    }

    public List<Course> getCoursesTaught() { return coursesTaught; }

    @Override
    public String getRole() {
        return "Professor";
    }
}
