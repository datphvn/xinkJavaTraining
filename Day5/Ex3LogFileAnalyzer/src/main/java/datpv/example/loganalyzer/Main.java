package datpv.example.loganalyzer;

import datpv.example.loganalyzer.engine.LogProcessor;

import java.nio.file.Path;
import java.time.Duration;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) throws Exception {
        Path logsDir = Path.of("data/logs");
        Path reportDir = Path.of("reports");

        // create processor with 4 threads
        LogProcessor processor = new LogProcessor(logsDir, reportDir, 4);

        // Add custom alert rule: if message contains "DB timeout" 3 times in 60s
        processor.addAlertRule(Pattern.compile("DB timeout", Pattern.CASE_INSENSITIVE), 3);

        // Start realtime monitoring (watch and tail)
        processor.startRealtimeMonitoring();

        // One-time processing of an existing file
        Path sample = Path.of("data/logs/sample-access.log");
        processor.processLogFile(sample);

        // Demo: run for 2 minutes then shutdown (adjust as needed)
        Thread.sleep(Duration.ofMinutes(2).toMillis());

        processor.shutdown();
        System.out.println("Finished. Reports in: " + reportDir.toAbsolutePath());
    }
}
