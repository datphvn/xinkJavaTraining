

public class Main {
    public static void main(String[] args) {
        Department cs = new Department("Computer Science");
        Professor p1 = new Professor("P1", "Dr. Smith", "smith@uni.edu");

        Course oop = new Course("CS101", "Object-Oriented Programming", 3, 2, "Mon 9-11");
        Course ds = new Course("CS102", "Data Structures", 3, 2, "Mon 9-11");
        ds.addPrerequisite(oop);

        cs.addCourse(oop);
        cs.addCourse(ds);
        cs.addProfessor(p1);
        oop.setProfessor(p1);

        Student s1 = new Student("S1", "Alice", "alice@uni.edu");
        Student s2 = new Student("S2", "Bob", "bob@uni.edu");
        Student s3 = new Student("S3", "Charlie", "charlie@uni.edu");


        oop.enroll(s1);
        oop.enroll(s2);
        oop.enroll(s3); // waitlist

        // Assign grades
        for (Enrollment e : oop.getEnrollments()) {
            e.assignGrade(Grade.A);
        }

        // Transcript
        Transcript.print(s1);

        // Conflict detection
        System.out.println("Conflict between OOP and DS: " + Scheduler.hasConflict(oop, ds));

        // Academic standing
        System.out.println("Alice standing: " + AcademicStanding.evaluate(s1));
    }
}
