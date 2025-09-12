
public class AcademicStanding {
    public static String evaluate(Student student) {
        double gpa = student.getGpa();
        if (gpa >= 3.7) return "Dean's List";
        else if (gpa >= 3.0) return "Honors";
        else if (gpa >= 2.0) return "Good Standing";
        else return "Probation";
    }
}
