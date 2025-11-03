package util;

import model.Holiday;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

/**
 * Utility class to manage business days and holidays.
 */
public class BusinessCalendar {
    private final List<Holiday> holidays;

    public BusinessCalendar(List<Holiday> holidays) {
        this.holidays = holidays;
    }

    /**
     * Checks if a given date is a weekend (Saturday or Sunday).
     * @param date The LocalDate to check.
     * @return true if it is a weekend.
     */
    public boolean isWeekend(LocalDate date) {
        DayOfWeek day = date.getDayOfWeek();
        return day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY;
    }

    /**
     * Checks if a given date is a holiday.
     * @param date The LocalDate to check.
     * @return true if it is one of the defined holidays.
     */
    public boolean isHoliday(LocalDate date) {
        // Sử dụng Stream và anyMatch để kiểm tra nhanh
        return holidays.stream()
                .anyMatch(holiday -> holiday.getDate().isEqual(date));
    }

    /**
     * Checks if a given date is a business day (not weekend and not holiday).
     * @param date The LocalDate to check.
     * @return true if it is a business day.
     */
    public boolean isBusinessDay(LocalDate date) {
        return !isWeekend(date) && !isHoliday(date);
    }

    /**
     * Finds the next working day after a given date.
     * @param date The starting date.
     * @return The next working day.
     */
    public LocalDate findNextBusinessDay(LocalDate date) {
        LocalDate nextDay = date.plusDays(1);
        while (!isBusinessDay(nextDay)) {
            nextDay = nextDay.plusDays(1);
        }
        return nextDay;
    }
}
