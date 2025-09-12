
public class Transcript {
    public static void print(Student student) {
        System.out.println("Transcript for " + student.getName());
        for (Enrollment e : student.getEnrollments()) {
            String grade = e.getGrade() != null ? e.getGrade().name() : "In Progress";
            System.out.println(e.getCourse().getCode() + " - " + e.getCourse().getTitle() + ": " + grade);
        }
        System.out.println("GPA: " + student.getGpa());
        System.out.println("Academic Standing: " + student.getAcademicStanding());
    }
}
