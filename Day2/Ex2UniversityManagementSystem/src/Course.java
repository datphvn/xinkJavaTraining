

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Course {
    private final String code;
    private final String title;
    private final int credits;
    private final List<Course> prerequisites;
    private final List<Enrollment> enrollments;
    private final Queue<Student> waitlist;
    private final int capacity;
    private final String schedule; // e.g., "Mon 9-11"
    private Professor professor;

    public Course(String code, String title, int credits, int capacity, String schedule) {
        this.code = code;
        this.title = title;
        this.credits = credits;
        this.capacity = capacity;
        this.schedule = schedule;
        this.prerequisites = new ArrayList<>();
        this.enrollments = new ArrayList<>();
        this.waitlist = new LinkedList<>();
    }

    public void addPrerequisite(Course c) { prerequisites.add(c); }
    public List<Course> getPrerequisites() { return prerequisites; }

    public void setProfessor(Professor p) { this.professor = p; }
    public Professor getProfessor() { return professor; }

    public String getCode() { return code; }
    public String getTitle() { return title; }
    public int getCredits() { return credits; }
    public String getSchedule() { return schedule; }

    public boolean hasSpace() { return enrollments.size() < capacity; }

    public void enroll(Student student) {
        if (!hasSpace()) {
            waitlist.add(student);
            System.out.println("Student " + student.getName() + " added to waitlist for " + title);
            return;
        }
        Enrollment e = new Enrollment(student, this);
        enrollments.add(e);
        student.addEnrollment(e);
        System.out.println("Student " + student.getName() + " enrolled in " + title);
    }

    public void drop(Student student) {
        enrollments.removeIf(e -> e.getStudent().equals(student));
        if (!waitlist.isEmpty()) {
            Student next = waitlist.poll();
            enroll(next);
        }
    }

    public List<Enrollment> getEnrollments() { return enrollments; }
    public Queue<Student> getWaitlist() { return waitlist; }
}
