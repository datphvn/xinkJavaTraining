package datpv.example.loganalyzer.parser;

import datpv.example.loganalyzer.model.LogEntry;

import java.time.OffsetDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Generic regex-based parser. Group indices are used to extract fields.
 * Example usage:
 *   new RegexLogParser("^(\\\\S+) (\\\\S+) (\\\\S+) (.*)$", 4, null, 2)
 */
public class RegexLogParser implements LogParser {
    private final Pattern pattern;
    private final Integer timeGroup;   // group index for time (optional)
    private final Integer levelGroup;  // group index for level (optional)
    private final Integer msgGroup;    // group index for message (optional)

    public RegexLogParser(String regex, Integer timeGroup, Integer levelGroup, Integer msgGroup) {
        this.pattern = Pattern.compile(regex);
        this.timeGroup = timeGroup;
        this.levelGroup = levelGroup;
        this.msgGroup = msgGroup;
    }

    @Override
    public Optional<LogEntry> parse(String line) {
        Matcher m = pattern.matcher(line);
        if (!m.find()) return Optional.empty();

        OffsetDateTime ts = OffsetDateTime.now();
        try {
            if (timeGroup != null) {
                String tsStr = m.group(timeGroup);
                // best-effort: not parsing complex timestamps here
                ts = OffsetDateTime.parse(tsStr);
            }
        } catch (Exception ignored) {}

        String level = levelGroup != null ? safeGroup(m, levelGroup) : "INFO";
        String msg = msgGroup != null ? safeGroup(m, msgGroup) : line;

        Map<String, Object> attrs = new HashMap<>();
        return Optional.of(new LogEntry(ts, level, msg, attrs));
    }

    private String safeGroup(Matcher m, int idx) {
        try { return m.group(idx); } catch (Exception e) { return null; }
    }
}
