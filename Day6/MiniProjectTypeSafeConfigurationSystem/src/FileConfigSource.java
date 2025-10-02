import java.io.FileInputStream;
import java.util.*;

public class FileConfigSource implements ConfigSource {
    @Override
    public Map<String, String> load(String location) throws Exception {
        Properties props = new Properties();
        props.load(new FileInputStream(location));

        Map<String, String> map = new HashMap<>();
        for (String name : props.stringPropertyNames()) {
            map.put(name, props.getProperty(name));
        }
        return map;
    }
}
