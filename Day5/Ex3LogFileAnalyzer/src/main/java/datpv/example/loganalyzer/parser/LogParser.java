package datpv.example.loganalyzer.parser;

import datpv.example.loganalyzer.model.LogEntry;

import java.util.Optional;

public interface LogParser {

    Optional<LogEntry> parse(String line);
}
