package service;

import model.Employee;
import java.time.LocalDate;
import java.time.Period;

/**
 * Handles payroll calculations using Period for long-term service and date-based math.
 */
public class PayrollService {

    /**
     * Calculates the length of service for an employee in Years, Months, and Days.
     * Uses Period.between().
     * * @param employee The employee object.
     * @param calculationDate The date to calculate service length up to (usually today).
     * @return The Period representing the length of service.
     */
    public Period calculateServiceLength(Employee employee, LocalDate calculationDate) {
        // Đảm bảo dữ liệu đầu vào không bị conflict: LocalDate là Immutable,
        // nên việc gọi hàm này không làm thay đổi các đối tượng Employee hay calculationDate.
        if (calculationDate.isBefore(employee.getHireDate())) {
            return Period.ZERO;
        }
        return Period.between(employee.getHireDate(), calculationDate);
    }

    /**
     * Calculates the employee's exact current age based on hire date.
     * The Period calculation is precise in terms of years, months, and days.
     * * @param employee The employee object.
     * @param calculationDate The current date.
     * @return A formatted string showing service length.
     */
    public String formatServiceLength(Employee employee, LocalDate calculationDate) {
        Period service = calculateServiceLength(employee, calculationDate);
        return String.format("%d years, %d months, and %d days",
                service.getYears(), service.getMonths(), service.getDays());
    }

    /**
     * Calculates the salary for a partial month based on daily rate.
     * This demonstrates using day calculation within a period.
     * * @param employee The employee object.
     * @param startDate The first day of employment in the month.
     * @param endDate The last day of the calculation period.
     * @return The partial salary amount.
     */
    public double calculatePartialMonthSalary(Employee employee, LocalDate startDate, LocalDate endDate) {
        if (startDate.isAfter(endDate)) {
            return 0.0;
        }

        // 1. Tính tổng số ngày trong tháng (Giả định tháng hiện tại là tháng của startDate)
        int daysInMonth = startDate.lengthOfMonth();

        // 2. Tính số ngày làm việc thực tế trong tháng
        long actualWorkDays = startDate.datesUntil(endDate.plusDays(1))
                .count();

        // 3. Tính lương theo ngày
        double dailyRate = employee.getSalaryPerMonth() / daysInMonth;

        return dailyRate * actualWorkDays;
    }
}
