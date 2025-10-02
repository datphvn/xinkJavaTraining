import java.util.*;

public class YamlConfigSource implements ConfigSource {
    @Override
    public Map<String, String> load(String location) throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("yaml.key", "yamlValue");
        return map;
    }
}
