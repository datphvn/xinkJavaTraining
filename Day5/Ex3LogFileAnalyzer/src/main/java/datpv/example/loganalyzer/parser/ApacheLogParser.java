package datpv.example.loganalyzer.parser;

import datpv.example.loganalyzer.model.LogEntry;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Basic Apache/Nginx common log parser (Combined/Common Log Format).
 * This is a permissive implementation for demonstration.
 */
public class ApacheLogParser implements LogParser {
    // Example:
    // 127.0.0.1 - - [10/Oct/2023:13:55:36 -0700] "GET /index.html HTTP/1.1" 200 1024
    private static final Pattern PAT = Pattern.compile("^(\\S+) \\S+ \\S+ \\[([^\\]]+)] \"([^\"]+)\" (\\d{3}) (\\S+)(.*)$");
    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss Z", Locale.ENGLISH);

    @Override
    public Optional<LogEntry> parse(String line) {
        try {
            Matcher m = PAT.matcher(line);
            if (!m.find()) return Optional.empty();

            String host = m.group(1);
            String dateStr = m.group(2);
            String request = m.group(3);
            String status = m.group(4);
            String bytes = m.group(5);
            String trailing = m.group(6).trim();

            OffsetDateTime ts;
            try {
                ts = OffsetDateTime.parse(dateStr, DTF);
            } catch (Exception ex) {
                ts = OffsetDateTime.now();
            }

            Map<String, Object> attrs = new HashMap<>();
            attrs.put("remote", host);
            attrs.put("request", request);
            attrs.put("status", Integer.parseInt(status));
            if (!"-".equals(bytes)) {
                try { attrs.put("bytes", Long.parseLong(bytes)); } catch (NumberFormatException ignored) {}
            }
            if (!trailing.isEmpty()) attrs.put("extra", trailing);

            String message = request + " -> " + status;
            return Optional.of(new LogEntry(ts, "INFO", message, attrs));
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
