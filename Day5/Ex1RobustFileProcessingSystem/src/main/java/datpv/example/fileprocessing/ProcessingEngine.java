package datpv.example.fileprocessing;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

public class ProcessingEngine {
    private final ExecutorService executor = Executors.newFixedThreadPool(4);
    private final Map<String, FileProcessingSystem.FileProcessor> processors = new HashMap<>();

    public ProcessingEngine() {
        processors.put("csv", new CSVProcessor());
        processors.put("json", new JSONProcessor());
        processors.put("xml", new XMLProcessor());
    }

    public CompletableFuture<ProcessingResult> processFile(Path file, Path outputDir) {
        return CompletableFuture.supplyAsync(() -> {
            String ext = getFileExtension(file.getFileName().toString());
            FileProcessingSystem.FileProcessor processor = processors.get(ext);

            if (processor == null) {
                throw new CompletionException(new ProcessingException("Unsupported file type: " + ext));
            }

            try {
                return RetryPolicy.executeWithRetry(() ->
                        processor.process(file, outputDir), 3, 500);
            } catch (Exception e) {
                throw new CompletionException(new ProcessingException("Processing failed", e));
            }
        }, executor);
    }

    private String getFileExtension(String name) {
        int dot = name.lastIndexOf('.');
        return (dot == -1) ? "" : name.substring(dot + 1).toLowerCase();
    }
}
