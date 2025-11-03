import model.Employee;
import model.Holiday;
import model.Meeting;
import model.RecurringEvent;
import service.MeetingScheduler;
import service.PayrollService;
import service.RecurringEventService;
import util.BusinessCalendar;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * Main application to demonstrate Advanced Date/Time API Projects.
 * Includes: Global Meeting Scheduling, Payroll, Recurring Events, and Business Calendar.
 */
class MainApp {

    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println("  JAVA 8 DATE/TIME & OPTIONAL ADVANCED PROJECTS");
        System.out.println("=================================================");

        // --- 1. Global Meeting Scheduler (ZonedDateTime & ZoneId) ---
        runMeetingSchedulerDemo();

        // --- 2. Payroll System (Period & Date Math) ---
        runPayrollDemo();

        // --- 3. Recurring Event System (TemporalAdjusters) ---
        runRecurringEventDemo();

        // --- 4. Business Calendar (Stream & Filters) ---
        runBusinessCalendarDemo();
    }

    private static void runMeetingSchedulerDemo() {
        System.out.println("\n--- 1. Global Meeting Scheduler ---");

        // Cuộc họp gốc: 14:00 ngày 20/03/2025 tại New York
        ZoneId newYork = ZoneId.of("America/New_York");
        LocalDateTime startLocal = LocalDateTime.of(2025, 3, 20, 14, 0);
        LocalDateTime endLocal = startLocal.plusHours(1);

        Meeting teamSync = new Meeting("Q1 Team Sync", startLocal, endLocal, newYork);
        System.out.println("Original Meeting: " + teamSync);

        MeetingScheduler scheduler = new MeetingScheduler();

        // Danh sách người tham gia và múi giờ của họ
        Map<String, ZoneId> participants = new HashMap<>();
        participants.put("Alice (NY)", ZoneId.of("America/New_York"));
        participants.put("Ben (London)", ZoneId.of("Europe/London"));
        participants.put("Minh (Hanoi)", ZoneId.of("Asia/Ho_Chi_Minh"));
        participants.put("Ken (Tokyo)", ZoneId.of("Asia/Tokyo"));

        // Lên lịch họp
        Map<String, ZonedDateTime> scheduledTimes = scheduler.scheduleGlobalMeeting(teamSync, participants);
        scheduler.displayScheduledTimes(scheduledTimes);

        // Lưu ý: Nếu thời điểm này rơi vào DST, ZonedDateTime sẽ tự động điều chỉnh.
    }

    private static void runPayrollDemo() {
        System.out.println("\n--- 2. Payroll System (Period & Date Math) ---");

        // Ngày hiện tại giả định
        LocalDate calculationDate = LocalDate.of(2025, 5, 20);

        // Nhân viên 1: Lương cố định
        Employee john = new Employee("E001", "John Doe",
                LocalDate.of(2022, 10, 15), 50000.0);

        // Nhân viên 2: Mới vào làm giữa tháng
        Employee jane = new Employee("E002", "Jane Smith",
                LocalDate.of(2025, 5, 10), 60000.0);

        PayrollService payroll = new PayrollService();

        // Tính thời gian làm việc (Period)
        String johnService = payroll.formatServiceLength(john, calculationDate);
        System.out.printf("John's Service Length (%s): %s%n", john.getHireDate(), johnService);

        // Tính lương tháng đầu tiên của Jane (Partial Month Salary)
        LocalDate janeStart = jane.getHireDate();
        LocalDate monthEnd = LocalDate.of(2025, 5, 31);
        double janePartialSalary = payroll.calculatePartialMonthSalary(jane, janeStart, monthEnd);

        System.out.printf("Jane's Partial Salary (%s - %s): $%.2f%n",
                janeStart, monthEnd, janePartialSalary);
    }

    private static void runRecurringEventDemo() {
        System.out.println("\n--- 3. Recurring Event System (TemporalAdjusters) ---");

        RecurringEventService eventService = new RecurringEventService();
        LocalDate baseDate = LocalDate.of(2025, 1, 1);

        // 3.1. Sự kiện lặp lại sử dụng Adjuster có sẵn (Next Monday)
        RecurringEvent nextMonday = new RecurringEvent(
                "Weekly Report",
                baseDate, // 2025-01-01 (Wednesday)
                "NEXT_MONDAY",
                TemporalAdjusters.next(DayOfWeek.MONDAY)
        );

        List<LocalDate> weeklyReports = eventService.calculateNextOccurrences(nextMonday, 5);
        System.out.println("Next 5 Weekly Reports (Next Monday):");
        weeklyReports.forEach(date -> System.out.println(" - " + date));

        // 3.2. Sự kiện lặp lại sử dụng Adjuster phức tạp (Last Day of Month)
        RecurringEvent monthEnd = new RecurringEvent(
                "Month End Check",
                baseDate,
                "LAST_DAY_OF_MONTH",
                TemporalAdjusters.lastDayOfMonth()
        );

        List<LocalDate> monthEndChecks = eventService.calculateNextOccurrences(monthEnd, 3);
        System.out.println("\nNext 3 Month End Checks (Last Day of Month):");
        monthEndChecks.forEach(date -> System.out.println(" - " + date));
    }

    private static void runBusinessCalendarDemo() {
        System.out.println("\n--- 4. Business Calendar (Holiday Management) ---");

        // Định nghĩa ngày lễ
        List<Holiday> holidays = Arrays.asList(
                new Holiday("New Year's Day", LocalDate.of(2025, 1, 1)),
                new Holiday("Independence Day", LocalDate.of(2025, 9, 2)),
                new Holiday("Weekend Holiday", LocalDate.of(2025, 9, 7)) // Saturday
        );

        BusinessCalendar calendar = new BusinessCalendar(holidays);

        // Các ngày cần kiểm tra
        LocalDate date1 = LocalDate.of(2025, 1, 1);     // Thứ Tư (Holiday)
        LocalDate date2 = LocalDate.of(2025, 1, 2);     // Thứ Năm (Business Day)
        LocalDate date3 = LocalDate.of(2025, 1, 4);     // Thứ Bảy (Weekend)
        LocalDate date4 = LocalDate.of(2025, 9, 6);     // Thứ Sáu (Business Day)

        System.out.printf("Is %s a business day? %b (Holiday: %b)%n",
                date1, calendar.isBusinessDay(date1), calendar.isHoliday(date1));
        System.out.printf("Is %s a business day? %b%n",
                date2, calendar.isBusinessDay(date2));
        System.out.printf("Is %s a business day? %b (Weekend: %b)%n",
                date3, calendar.isBusinessDay(date3), calendar.isWeekend(date3));

        // Tìm ngày làm việc tiếp theo
        LocalDate nextWorkingDayAfterHoliday = calendar.findNextBusinessDay(date1);
        System.out.printf("\nNext working day after %s is: %s%n",
                date1, nextWorkingDayAfterHoliday); // 2025-01-02

        LocalDate nextWorkingDayAfterFriday = calendar.findNextBusinessDay(date4);
        System.out.printf("Next working day after %s (Friday) is: %s%n",
                date4, nextWorkingDayAfterFriday); // Sẽ nhảy qua Saturday, Sunday, Holiday (9/7) và Independence Day (9/2)
    }
}
