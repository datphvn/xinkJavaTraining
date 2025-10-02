import java.util.Map;

public interface ConfigSource {
    Map<String, String> load(String location) throws Exception;
}
