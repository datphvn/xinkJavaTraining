package datpv.example.fileprocessing;

import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import java.nio.file.Path;

public class XMLProcessor implements FileProcessingSystem.FileProcessor {
    @Override
    public ProcessingResult process(Path inputFile, Path outputDir) throws ProcessingException {
        try {
            Document doc = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder()
                    .parse(inputFile.toFile());

            String root = doc.getDocumentElement().getNodeName();
            return new ProcessingResult(true,
                    "XML processed successfully. Root element: " + root);
        } catch (Exception e) {
            throw new ProcessingException("XML processing failed", e);
        }
    }
}
