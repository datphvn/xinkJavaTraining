package datpv.example.fileprocessing;

import java.io.*;
import java.nio.file.*;
import java.util.stream.Stream;

public class CSVProcessor implements FileProcessingSystem.FileProcessor {
    @Override
    public ProcessingResult process(Path inputFile, Path outputDir) throws ProcessingException {
        try {
            long lineCount;
            try (Stream<String> lines = Files.lines(inputFile)) {
                lineCount = lines.count();
            }

            return new ProcessingResult(true,
                    "CSV processed successfully. Lines: " + lineCount);
        } catch (IOException e) {
            throw new ProcessingException("CSV processing failed", e);
        }
    }
}
