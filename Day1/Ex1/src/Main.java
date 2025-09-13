import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static StudentManagementSystem sms = new StudentManagementSystem();

    public static void main(String[] args) {
        System.out.println("Student Management System - Menu");
        // pre-populate sample (optional)
        sms.populateSampleData();

        while (true) {
            printMenu();
            String choice = scanner.nextLine().trim();
            try {
                switch (choice) {
                    case "1": addStudent(); break;
                    case "2": addCourse(); break;
                    case "3": addPrerequisite(); break;
                    case "4": registerCourse(); break;
                    case "5": assignGrade(); break;
                    case "6": printTranscript(); break;
                    case "7": listStudents(); break;
                    case "8": listCourses(); break;
                    case "9": searchStudent(); break;
                    case "10": checkGraduation(); break;
                    case "11": saveData(); break;
                    case "12": loadData(); break;
                    case "13": autoTest(); break;
                    case "0": System.out.println("Bye"); return;
                    default: System.out.println("Invalid choice"); break;
                }
            } catch (Exception ex) {
                System.out.println("Error: " + ex.getMessage());
            }
        }
    }

    private static void printMenu() {
        System.out.println("\nMenu:");
        System.out.println("1. Add Student");
        System.out.println("2. Add Course");
        System.out.println("3. Add Prerequisite (course <- prereq)");
        System.out.println("4. Register Student to Course");
        System.out.println("5. Assign Grade");
        System.out.println("6. Print Transcript");
        System.out.println("7. List Students");
        System.out.println("8. List Courses");
        System.out.println("9. Search Student by Name");
        System.out.println("10. Check Graduation (studentId required)");
        System.out.println("11. Save Data");
        System.out.println("12. Load Data");
        System.out.println("13. Run AutoTest (sample scenario)");
        System.out.println("0. Exit");
        System.out.print("Choose: ");
    }

    private static void addStudent() {
        System.out.print("StudentId: "); String id = scanner.nextLine().trim();
        System.out.print("First name: "); String fn = scanner.nextLine().trim();
        System.out.print("Last name: "); String ln = scanner.nextLine().trim();
        System.out.print("Birth yyyy-mm-dd: "); LocalDate bd = LocalDate.parse(scanner.nextLine().trim());
        Student s = new Student(id, fn, ln, bd);
        System.out.print("Email: "); s.setEmail(scanner.nextLine().trim());
        sms.addStudent(s);
        System.out.println("Added: " + s);
    }

    private static void addCourse() {
        System.out.print("Course code: "); String code = scanner.nextLine().trim();
        System.out.print("Course name: "); String name = scanner.nextLine().trim();
        System.out.print("Credits: "); int credits = Integer.parseInt(scanner.nextLine().trim());
        System.out.print("Max capacity: "); int cap = Integer.parseInt(scanner.nextLine().trim());
        Course c = new Course(code, name, credits, cap);
        System.out.println("Optionally add schedule (e.g. Mon 9). Press Enter to skip.");
        System.out.print("Day (or blank): ");
        String day = scanner.nextLine().trim();
        if (!day.isEmpty()) {
            System.out.print("Hour (int): ");
            int hour = Integer.parseInt(scanner.nextLine().trim());
            c.addSchedule(day, hour);
        }
        sms.addCourse(c);
        System.out.println("Added: " + c);
    }

    private static void addPrerequisite() {
        System.out.print("Course code: "); String course = scanner.nextLine().trim();
        System.out.print("Prerequisite course code: "); String prereq = scanner.nextLine().trim();
        sms.addPrerequisite(course, prereq);
        System.out.println("Added prerequisite " + prereq + " -> " + course);
    }

    private static void registerCourse() {
        System.out.print("StudentId: "); String sid = scanner.nextLine().trim();
        System.out.print("Course code: "); String cc = scanner.nextLine().trim();
        Student s = sms.findStudentById(sid);
        Course c = sms.findCourseByCode(cc);
        if (s == null || c == null) {
            System.out.println("Student or Course not found");
            return;
        }
        boolean ok = sms.registerCourse(s, c);
        System.out.println("registerCourse returned: " + ok);
    }

    private static void assignGrade() {
        System.out.print("StudentId: "); String sid = scanner.nextLine().trim();
        System.out.print("Course code: "); String cc = scanner.nextLine().trim();
        System.out.print("Grade (0-10): "); double g = Double.parseDouble(scanner.nextLine().trim());
        boolean ok = sms.assignGrade(sid, cc, g);
        System.out.println("assignGrade returned: " + ok);
    }

    private static void printTranscript() {
        System.out.print("StudentId: "); String sid = scanner.nextLine().trim();
        Student s = sms.findStudentById(sid);
        if (s == null) { System.out.println("not found"); return; }
        System.out.println(s.generateTranscript());
    }

    private static void listStudents() { sms.listStudents(); }
    private static void listCourses() { sms.listCourses(); }

    private static void searchStudent() {
        System.out.print("Name keyword: ");
        String k = scanner.nextLine().trim();
        List<Student> found = sms.searchStudentByName(k);
        System.out.println("Found " + found.size());
        for (Student s : found) System.out.println(" - " + s);
    }

    private static void checkGraduation() {
        System.out.print("StudentId: "); String sid = scanner.nextLine().trim();
        System.out.print("Required credits (int): "); int rc = Integer.parseInt(scanner.nextLine().trim());
        System.out.print("Min GPA (double): "); double gpa = Double.parseDouble(scanner.nextLine().trim());
        Student s = sms.findStudentById(sid);
        if (s == null) { System.out.println("not found"); return; }
        System.out.println("Eligible: " + s.checkGraduationRequirements(rc, gpa));
    }

    private static void saveData() {
        System.out.print("File path to save: "); String path = scanner.nextLine().trim();
        try {
            sms.saveData(path);
            System.out.println("Saved to " + path);
        } catch (IOException e) {
            System.out.println("Save failed: " + e.getMessage());
        }
    }

    private static void loadData() {
        System.out.print("File path to load: "); String path = scanner.nextLine().trim();
        try {
            StudentManagementSystem loaded = StudentManagementSystem.loadData(path);
            if (loaded != null) {
                sms = loaded;
                System.out.println("Loaded system from " + path);
            }
        } catch (Exception e) {
            System.out.println("Load failed: " + e.getMessage());
        }
    }

    // Auto test reproduces your original example + some asserts printed
    private static void autoTest() {
        System.out.println("Running AutoTest scenario (like original main)...");
        // clear current system and populate fresh
        sms = new StudentManagementSystem();
        sms.populateSampleData();
        // fetch students and courses
        Student s1 = sms.findStudentById("S001");
        Student s2 = sms.findStudentById("S002");
        Course c1 = sms.findCourseByCode("C101");
        Course c2 = sms.findCourseByCode("C102");

        System.out.println("Registering:");
        System.out.println("S1 -> Java: " + sms.registerCourse(s1, c1));
        System.out.println("S2 -> Java: " + sms.registerCourse(s2, c1));
        System.out.println("S1 -> Database: " + sms.registerCourse(s1, c2));
        System.out.println("S2 -> Database (should be waitlisted): " + sms.registerCourse(s2, c2));

        // assign grades
        sms.assignGrade("S001", "C101", 8.0);
        sms.assignGrade("S001", "C102", 8.0);
        sms.assignGrade("S002", "C101", 6.5);
        // S002 is waitlisted for C102 -> assignGrade should fail
        sms.assignGrade("S002", "C102", 6.5);

        System.out.println("\nTranscripts:");
        System.out.println(s1.generateTranscript());
        System.out.println(s2.generateTranscript());

        System.out.println("\nGPAs:");
        System.out.println("S1 GPA: " + s1.calculateGPA());
        System.out.println("S2 GPA: " + s2.calculateGPA());

        System.out.println("\nGraduation test (>=5 credits, GPA>=5.0):");
        System.out.println("S1: " + s1.checkGraduationRequirements(5, 5.0));
        System.out.println("S2: " + s2.checkGraduationRequirements(5, 5.0));
    }
}
