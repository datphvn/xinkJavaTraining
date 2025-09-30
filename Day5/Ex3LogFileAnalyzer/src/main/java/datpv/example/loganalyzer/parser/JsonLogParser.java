package datpv.example.loganalyzer.parser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import datpv.example.loganalyzer.model.LogEntry;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class JsonLogParser implements LogParser {
    private final ObjectMapper mapper = new ObjectMapper();
    private final DateTimeFormatter[] formats = new DateTimeFormatter[] {
            DateTimeFormatter.ISO_OFFSET_DATE_TIME,
            DateTimeFormatter.ISO_LOCAL_DATE_TIME
    };

    @Override
    public Optional<LogEntry> parse(String line) {
        try {
            JsonNode node = mapper.readTree(line);
            String tsStr = node.has("timestamp") ? node.get("timestamp").asText() : null;
            OffsetDateTime ts = parseTimestamp(tsStr).orElse(OffsetDateTime.now());

            String level = node.has("level") ? node.get("level").asText() : "INFO";
            String msg = node.has("message") ? node.get("message").asText() : node.toString();

            Map<String, Object> attrs = mapper.convertValue(node, Map.class);
            return Optional.of(new LogEntry(ts, level, msg, attrs));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private Optional<OffsetDateTime> parseTimestamp(String ts) {
        if (ts == null) return Optional.empty();
        for (DateTimeFormatter f : formats) {
            try {
                return Optional.of(OffsetDateTime.parse(ts, f));
            } catch (Exception ignored) {}
        }
        return Optional.empty();
    }
}
