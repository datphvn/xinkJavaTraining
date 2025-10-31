package com.bigdata.processors;

import com.bigdata.model.RawRecord;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.stream.Stream;

public class FileChunker {

    // Creates a memory-efficient stream from a large file
    public static Stream<RawRecord> streamFile(Path path) {
        try {
            // Files.lines provides the core memory-efficient, lazy source
            return Files.lines(path)
                    .skip(1) // Skip header line
                    .map(line -> line.split(","))
                    .filter(fields -> fields.length >= 3) // Basic integrity check
                    .map(fields -> new RawRecord(0, fields)); // Note: Line number tracking simplified
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to stream file: " + path, e);
        }
    }

    // Utility to create a large dummy file for testing
    public static void createDummyFile(Path path, int lines) throws IOException {
        try (var writer = Files.newBufferedWriter(path)) {
            writer.write("timestamp,category,value\n");
            for (int i = 1; i <= lines; i++) {
                // Introduce some intentional errors for testing validation/error handling
                String category = (i % 1000 == 0) ? "" : "CAT-" + (i % 10);
                String value = (i % 500 == 0) ? "invalid" : String.format("%.2f", Math.random() * 100);

                writer.write(String.format("%s,%s,%s\n",
                        LocalDateTime.now().minusMinutes(lines - i).toString(),
                        category, value));
            }
        }
    }
}