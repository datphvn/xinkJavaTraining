package model;

import java.time.LocalDate;
import java.time.DayOfWeek;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;

/**
 * Model for a recurring event definition.
 * Holds the start date and the TemporalAdjuster logic for recurrence.
 */
public class RecurringEvent {
    private String name;
    private LocalDate startDate;
    private String recurrenceLogic; // Example: "LAST_DAY_OF_MONTH", "NEXT_MONDAY"

    // The core of recurring logic: TemporalAdjuster
    private final TemporalAdjuster adjuster;

    public RecurringEvent(String name, LocalDate startDate, String recurrenceLogic, TemporalAdjuster adjuster) {
        this.name = name;
        this.startDate = startDate;
        this.recurrenceLogic = recurrenceLogic;
        this.adjuster = adjuster;
    }

    // Getters and Setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public String getRecurrenceLogic() {
        return recurrenceLogic;
    }

    public void setRecurrenceLogic(String recurrenceLogic) {
        this.recurrenceLogic = recurrenceLogic;
    }

    public TemporalAdjuster getAdjuster() {
        return adjuster;
    }

    // Note: Setter for adjuster is excluded as it changes core logic,
    // better to create a new RecurringEvent object.
}
