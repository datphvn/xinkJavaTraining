package service;

import model.RecurringEvent;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles the generation of recurring events using TemporalAdjusters.
 */
public class RecurringEventService {

    /**
     * Generates a custom TemporalAdjuster to find the Nth DayOfWeek in a month.
     * Ví dụ: First Monday, Second Tuesday...
     * @param n The occurrence index (1 for first, 2 for second...).
     * @param dayOfWeek The DayOfWeek to find.
     * @return A custom TemporalAdjuster.
     */
    public TemporalAdjuster nthDayOfWeekInMonth(int n, DayOfWeek dayOfWeek) {
        // Tái sử dụng TemporalAdjuster có sẵn (dayOfWeekInMonth)
        return TemporalAdjusters.dayOfWeekInMonth(n, dayOfWeek);
    }

    /**
     * Generates a custom TemporalAdjuster to find the next business day (using an external calendar).
     * This demonstrates creating a custom TemporalAdjuster (lưu ý: chỉ là minh họa cách tạo, logic
     * thực tế phức tạp hơn sẽ được đặt trong BusinessCalendar).
     * @param calendar The business calendar utility.
     * @return A custom TemporalAdjuster.
     */
    public TemporalAdjuster nextBusinessDayAdjuster(util.BusinessCalendar calendar) {
        return (temporal) -> {
            LocalDate date = LocalDate.from(temporal);
            return calendar.findNextBusinessDay(date.minusDays(1)); // Logic tìm ngày làm việc tiếp theo
        };
    }

    /**
     * Calculates the next N occurrences of a recurring event.
     * * @param event The recurring event definition.
     * @param count The number of future occurrences to generate.
     * @return A list of future event dates.
     */
    public List<LocalDate> calculateNextOccurrences(RecurringEvent event, int count) {
        List<LocalDate> dates = new ArrayList<>();
        LocalDate current = event.getStartDate();

        for (int i = 0; i < count; i++) {
            // Apply the TemporalAdjuster to find the next date
            LocalDate nextDate = current.with(event.getAdjuster());

            // Đảm bảo rằng sự kiện tiếp theo là sau sự kiện hiện tại (tránh lặp vô hạn nếu adjuster trả về ngày hiện tại)
            if (nextDate.isEqual(current)) {
                // Nếu adjuster trả về ngày hiện tại, ta nhảy sang ngày tiếp theo (theo tháng/năm)
                if (event.getAdjuster() == TemporalAdjusters.lastDayOfMonth()) {
                    current = current.plusMonths(1).withDayOfMonth(1); // Chuyển sang tháng tiếp theo
                } else if (event.getAdjuster() == TemporalAdjusters.next(DayOfWeek.MONDAY)) {
                    current = current.plusDays(1);
                } else {
                    current = current.plusDays(1); // fallback
                }
                nextDate = current.with(event.getAdjuster());
            }

            // Ghi nhận sự kiện và chuẩn bị cho lần lặp tiếp theo
            dates.add(nextDate);
            current = nextDate;
        }

        return dates;
    }
}
