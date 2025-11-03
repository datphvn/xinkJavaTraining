package model;

import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * Model class for a Meeting.
 * Represents a meeting's local date/time and its original timezone.
 */
public class Meeting {
    private String title;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private ZoneId originalZone;

    public Meeting(String title, LocalDateTime startTime, LocalDateTime endTime, ZoneId originalZone) {
        this.title = title;
        this.startTime = startTime;
        this.endTime = endTime;
        this.originalZone = originalZone;
    }

    // Getters and Setters (Đảm bảo dữ liệu đầu vào không bị conflict)

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    // Input data is validated implicitly by the type (LocalDateTime is immutable)
    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public ZoneId getOriginalZone() {
        return originalZone;
    }

    public void setOriginalZone(ZoneId originalZone) {
        this.originalZone = originalZone;
    }

    @Override
    public String toString() {
        return String.format("%s [Start: %s %s, End: %s %s]",
                title, startTime, originalZone, endTime, originalZone);
    }
}
