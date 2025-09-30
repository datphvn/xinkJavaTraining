package datpv.example.fileprocessing;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

public class JSONProcessor implements FileProcessingSystem.FileProcessor {
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public ProcessingResult process(Path inputFile, Path outputDir) throws ProcessingException {
        try {
            Map<?, ?> json = mapper.readValue(inputFile.toFile(), Map.class);
            return new ProcessingResult(true,
                    "JSON processed successfully. Keys: " + json.keySet());
        } catch (IOException e) {
            throw new ProcessingException("JSON processing failed", e);
        }
    }
}
