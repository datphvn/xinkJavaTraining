package datpv.example.fileprocessing;

import java.nio.file.Path;

public class FileProcessingSystem {
    public interface FileProcessor {
        ProcessingResult process(Path inputFile, Path outputDir) throws ProcessingException;
    }

    public static void main(String[] args) throws Exception {
        ProcessingEngine engine = new ProcessingEngine();

//        Path input = Path.of("data/sample.csv");
        Path input = Path.of("data/sample.json");
//        Path input = Path.of("data/sample.xml");

        Path output = Path.of("output");

        ProcessingResult result = engine.processFile(input, output).get();
        System.out.println(result);
    }
}
