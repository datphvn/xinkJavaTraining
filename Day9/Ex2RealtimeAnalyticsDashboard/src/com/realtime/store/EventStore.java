package com.realtime.store;

import com.realtime.model.Event;
import com.realtime.model.HistoricalRecord;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EventStore {
    // Lưu trữ tạm thời cho các tính toán cửa sổ trượt
    private final CopyOnWriteArrayList<Event> recentEvents = new CopyOnWriteArrayList<>();
    // Lưu trữ lịch sử (dạng String đơn giản)
    private final CopyOnWriteArrayList<HistoricalRecord> history = new CopyOnWriteArrayList<>();

    public void ingestEvent(Event event) {
        recentEvents.add(event);
        // Store raw event as historical record (dùng toSimpleString())
        history.add(new HistoricalRecord(event.getTimestamp(), "Event", event.toSimpleString()));
    }

    public Stream<Event> getEventsInWindow(LocalDateTime start, LocalDateTime end) {
        // Stream filters on the concurrent list for sliding window calculation
        return recentEvents.stream()
                .filter(event -> event.getTimestamp().isAfter(start) && event.getTimestamp().isBefore(end));
    }

    public void cleanupOldEvents(LocalDateTime cutoff) {
        // Lọc và xóa các bản ghi cũ từ recentEvents
        List<Event> toRemove = recentEvents.stream()
                .filter(event -> event.getTimestamp().isBefore(cutoff))
                .collect(Collectors.toList());
        recentEvents.removeAll(toRemove);

        System.out.printf("Cleaned up %d old events from recent storage.%n", toRemove.size());

        // Cleanup history (tương thích ngược)
        history.removeIf(record -> record.getTimestamp().isBefore(cutoff.minusDays(30)));
    }
}