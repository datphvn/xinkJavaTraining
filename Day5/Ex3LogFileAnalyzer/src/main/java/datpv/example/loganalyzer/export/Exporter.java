package datpv.example.loganalyzer.export;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import datpv.example.loganalyzer.stats.LogStatistics;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public class Exporter {
    private final ObjectMapper mapper = new ObjectMapper();

    public void exportJson(LogStatistics stats, Path out) throws IOException {
        Map<String,Object> obj = Map.of(
                "total", stats.getTotal(),
                "errorCount", stats.getErrorCount(),
                "levels", stats.getLevelCounts(),
                "topMessages", stats.topMessages(10),
                "avgResponseTime", stats.averageResponseTime().orElse(Double.NaN),
                "errorRate", stats.errorRate()
        );
        mapper.writerWithDefaultPrettyPrinter().writeValue(out.toFile(), obj);
    }

    public void exportCsv(LogStatistics stats, Path out) throws IOException {
        try (BufferedWriter bw = Files.newBufferedWriter(out);
             CSVPrinter csv = new CSVPrinter(bw, CSVFormat.DEFAULT.withHeader("metric","value"))) {
            csv.printRecord("total", stats.getTotal());
            csv.printRecord("errorCount", stats.getErrorCount());
            for (Map.Entry<String, Long> e : stats.getLevelCounts().entrySet()) {
                csv.printRecord("level_" + e.getKey(), e.getValue());
            }
            csv.printRecord("avgResponseTime", stats.averageResponseTime().orElse(Double.NaN));
            csv.printRecord("errorRate", stats.errorRate());
        }
    }
}
