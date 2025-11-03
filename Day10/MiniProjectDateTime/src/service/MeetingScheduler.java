package service;

import model.Meeting;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Handles scheduling and time zone conversions for meetings.
 * Uses ZonedDateTime for accurate timezone management.
 */
public class MeetingScheduler {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z");

    /**
     * Converts a meeting time from its original time zone to a target time zone.
     * * @param meeting The original Meeting object.
     * @param targetZoneId The ZoneId to convert the meeting to.
     * @return The ZonedDateTime in the target time zone.
     */
    public ZonedDateTime convertMeetingTime(Meeting meeting, ZoneId targetZoneId) {
        // 1. Tạo ZonedDateTime từ thông tin gốc của cuộc họp
        ZonedDateTime originalZdt = meeting.getStartTime().atZone(meeting.getOriginalZone());

        // 2. Chuyển đổi múi giờ nhưng giữ nguyên Instant (thời điểm tuyệt đối)
        return originalZdt.withZoneSameInstant(targetZoneId);
    }

    /**
     * Finds the meeting times for a list of participants in their respective time zones.
     * * @param meeting The meeting to schedule.
     * @param participantTimeZones A map of participant names to their ZoneIds.
     * @return A map of participant names to their local ZonedDateTime.
     */
    public Map<String, ZonedDateTime> scheduleGlobalMeeting(
            Meeting meeting,
            Map<String, ZoneId> participantTimeZones) {

        Map<String, ZonedDateTime> scheduledTimes = new HashMap<>();

        for (Map.Entry<String, ZoneId> entry : participantTimeZones.entrySet()) {
            String participant = entry.getKey();
            ZoneId targetZone = entry.getValue();

            // Sử dụng hàm chuyển đổi đã tạo
            ZonedDateTime targetTime = convertMeetingTime(meeting, targetZone);

            scheduledTimes.put(participant, targetTime);
        }

        return scheduledTimes;
    }

    /**
     * Utility method to display scheduled times clearly.
     */
    public void displayScheduledTimes(Map<String, ZonedDateTime> scheduledTimes) {
        System.out.println("\n--- Global Meeting Schedule ---");
        scheduledTimes.forEach((name, time) ->
                System.out.printf("  %-15s: %s%n", name, time.format(FORMATTER)));
        System.out.println("-----------------------------");
    }
}
