import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        // Tạo hệ thống quản lý
        StudentManagementSystem sms = new StudentManagementSystem();

        // ====== Tạo sinh viên ======
        Student s1 = new Student("S001", "Nguyen", "An", LocalDate.of(2002, 5, 20));
        s1.setEmail("an.nguyen@example.com");

        Student s2 = new Student("S002", "Tran", "Binh", LocalDate.of(2001, 8, 15));
        s2.setEmail("binh.tran@example.com");

        sms.addStudent(s1);
        sms.addStudent(s2);

        // ====== Tạo môn học ======
        Course c1 = new Course("C101", "Java Programming", 3, 2);
        Course c2 = new Course("C102", "Database Systems", 4, 1);

        sms.addCourse(c1);
        sms.addCourse(c2);

        // ====== Đăng ký môn học ======
        System.out.println("== Đăng ký môn học ==");
        System.out.println("S1 -> Java: " + sms.registerCourse(s1, c1));
        System.out.println("S2 -> Java: " + sms.registerCourse(s2, c1));
        System.out.println("S1 -> Database: " + sms.registerCourse(s1, c2));
        System.out.println("S2 -> Database (sẽ vào waitlist): " + sms.registerCourse(s2, c2));

        // ====== Gán điểm ======
        for (Enrollment e : s1.getEnrollments()) {
            e.setGrade(8.0); // điểm 8
        }
        for (Enrollment e : s2.getEnrollments()) {
            e.setGrade(6.5); // điểm 6.5
        }

        // ====== In transcript ======
        System.out.println("\n== Transcript ==");
        System.out.println(s1.generateTranscript());
        System.out.println(s2.generateTranscript());

        // ====== Tính GPA và tốt nghiệp ======
        System.out.println("\n== GPA ==");
        System.out.println("S1 GPA: " + s1.calculateGPA());
        System.out.println("S2 GPA: " + s2.calculateGPA());

        System.out.println("\n== Kiểm tra tốt nghiệp (>= 5 tín chỉ, GPA >= 5.0) ==");
        System.out.println("S1 tốt nghiệp? " + s1.checkGraduationRequirements(5, 5.0));
        System.out.println("S2 tốt nghiệp? " + s2.checkGraduationRequirements(5, 5.0));

        // ====== Tìm kiếm sinh viên theo tên ======
        System.out.println("\n== Tìm kiếm sinh viên có tên 'Nguyen' ==");
        for (Student st : sms.searchStudentByName("Nguyen")) {
            System.out.println(st.getFirstName() + " " + st.getLastName());
        }
    }
}
