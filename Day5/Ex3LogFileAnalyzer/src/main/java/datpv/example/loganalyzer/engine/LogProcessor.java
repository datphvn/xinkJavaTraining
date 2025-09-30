package datpv.example.loganalyzer.engine;

import datpv.example.loganalyzer.export.Exporter;
import datpv.example.loganalyzer.model.LogEntry;
import datpv.example.loganalyzer.parser.*;
import datpv.example.loganalyzer.stats.LogStatistics;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.*;
import java.util.regex.Pattern;

public class LogProcessor {
    private final Path logsDir;
    private final Path reportDir;
    private final ExecutorService executor;
    private final List<LogParser> parsers = new CopyOnWriteArrayList<>();
    private final LogStatistics stats = new LogStatistics();
    private volatile boolean running = true;
    private final Exporter exporter = new Exporter();

    // pattern alerting: regex -> threshold count within window seconds
    private final Map<Pattern, Integer> alertRules = new ConcurrentHashMap<>();
    private final Map<Pattern, Deque<Instant>> occurrences = new ConcurrentHashMap<>();

    public LogProcessor(Path logsDir, Path reportDir, int threads) {
        this.logsDir = logsDir;
        this.reportDir = reportDir;
        this.executor = Executors.newFixedThreadPool(Math.max(1, threads));
        // default parsers (order matters)
        parsers.add(new JsonLogParser());
        parsers.add(new ApacheLogParser());
    }

    public void addParser(LogParser p) { parsers.add(p); }

    public void addAlertRule(Pattern p, int threshold) {
        alertRules.put(p, threshold);
        occurrences.putIfAbsent(p, new ArrayDeque<>());
    }

    /**
     * One-time processing of a file (streamed).
     */
    public void processLogFile(Path logFile) {
        if (logFile == null || !Files.exists(logFile)) return;
        executor.submit(() -> {
            System.out.println("Processing file: " + logFile);
            try (BufferedReader br = Files.newBufferedReader(logFile, StandardCharsets.UTF_8)) {
                String line;
                while ((line = br.readLine()) != null) {
                    handleLine(line);
                }
            } catch (IOException e) {
                System.err.println("Error reading file " + logFile + ": " + e.getMessage());
            }
        });
    }

    private void handleLine(String line) {
        Optional<LogEntry> entryOpt = Optional.empty();
        for (LogParser p : parsers) {
            entryOpt = p.parse(line);
            if (entryOpt.isPresent()) break;
        }
        entryOpt.ifPresent(entry -> {
            stats.accept(entry);
            checkAlerts(entry);
        });
    }

    private void checkAlerts(LogEntry entry) {
        String msg = entry.getMessage();
        Instant now = Instant.now();
        for (Map.Entry<Pattern,Integer> rule : alertRules.entrySet()) {
            Pattern pat = rule.getKey();
            int threshold = rule.getValue();
            if (pat.matcher(msg).find()) {
                Deque<Instant> dq = occurrences.computeIfAbsent(pat, k -> new ArrayDeque<>());
                synchronized (dq) {
                    dq.addLast(now);
                    // keep window (60s)
                    while (!dq.isEmpty() && dq.peekFirst().isBefore(now.minusSeconds(60))) dq.removeFirst();
                    if (dq.size() >= threshold) {
                        System.out.println("[ALERT] pattern " + pat + " occurred " + dq.size() + " times in 60s");
                        // clear to avoid immediate repeat alerts
                        dq.clear();
                    }
                }
            }
        }
    }

    /**
     * Start monitoring logs directory for real-time additions and tail appended lines.
     */
    public void startRealtimeMonitoring() throws IOException {
        if (!Files.exists(logsDir)) Files.createDirectories(logsDir);
        Thread watcherThread = new Thread(() -> {
            try (WatchService ws = FileSystems.getDefault().newWatchService()) {
                logsDir.register(ws, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_MODIFY);
                // tail existing files initially
                try (DirectoryStream<Path> ds = Files.newDirectoryStream(logsDir, "*.log")) {
                    for (Path p : ds) tailFile(p);
                } catch (IOException ignored) {}

                while (running) {
                    WatchKey key = ws.take();
                    for (WatchEvent<?> ev : key.pollEvents()) {
                        WatchEvent.Kind<?> kind = ev.kind();
                        Path name = (Path) ev.context();
                        Path full = logsDir.resolve(name);
                        if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
                            // new file - start tailing
                            tailFile(full);
                        } else if (kind == StandardWatchEventKinds.ENTRY_MODIFY) {
                            // modified - ensure tailing
                            tailFile(full);
                        }
                    }
                    key.reset();
                }
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, "LogWatcher");
        watcherThread.setDaemon(true);
        watcherThread.start();
    }

    /**
     * Tail file content from its end (like `tail -F`). Handles simple rotation by detecting file length decrease.
     */
    private void tailFile(Path file) {
        // ensure one tail task per file: submit but allow duplicates - tasks harmless
        executor.submit(() -> {
            try (RandomAccessFile raf = new RandomAccessFile(file.toFile(), "r")) {
                long pointer = raf.length();
                raf.seek(pointer);
                while (running) {
                    String raw = raf.readLine();
                    if (raw == null) {
                        long len = raf.length();
                        if (len < pointer) {
                            // rotated/truncated -> reopen from start
                            raf.seek(0);
                            pointer = 0;
                        } else {
                            Thread.sleep(200);
                        }
                    } else {
                        // RandomAccessFile.readLine returns ISO-8859-1 bytes - convert to UTF-8 correctly
                        String line = new String(raw.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
                        handleLine(line);
                        pointer = raf.getFilePointer();
                    }
                }
            } catch (FileNotFoundException fnf) {
                // file may have been rotated away
            } catch (IOException | InterruptedException e) {
                System.err.println("Tail error for " + file + ": " + e.getMessage());
            }
        });
    }

    public void exportReports() {
        try {
            if (!Files.exists(reportDir)) Files.createDirectories(reportDir);
            Path jsonOut = reportDir.resolve("stats.json");
            Path csvOut = reportDir.resolve("stats.csv");
            exporter.exportJson(stats, jsonOut);
            exporter.exportCsv(stats, csvOut);
            System.out.println("Reports exported to " + reportDir);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void shutdown() {
        running = false;
        executor.shutdown();
        try {
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) executor.shutdownNow();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        exportReports();
    }
}
