import java.util.Map;

public class EnvConfigSource implements ConfigSource {
    @Override
    public Map<String, String> load(String location) {
        return System.getenv();
    }
}
