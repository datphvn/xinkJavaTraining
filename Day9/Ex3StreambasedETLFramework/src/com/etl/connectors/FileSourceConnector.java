package com.etl.connectors;

import com.etl.core.DataConnector;
import com.etl.model.ETLConfig;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

// Connector đọc file (E)
public class FileSourceConnector implements DataConnector<String> {
    private String filePath;

    @Override
    public void configure(ETLConfig config) {
        this.filePath = config.getProperty("file.path");
    }

    @Override
    public Stream<String> extract() throws IOException {
        if (filePath == null) {
            throw new IOException("File path not configured.");
        }
        Path path = Paths.get(filePath);
        // Files.lines() là cơ chế Extraction Memory-efficient
        return Files.lines(path);
    }
}