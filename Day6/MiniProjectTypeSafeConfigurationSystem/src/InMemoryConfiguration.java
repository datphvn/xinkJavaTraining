import java.util.*;

public class InMemoryConfiguration implements ConfigurationSystem {
    private final Map<String, Object> values = new HashMap<>();

    @Override
    public <T> T get(ConfigKey<T> key) {
        Object val = values.get(key.getKey());
        return (val == null) ? key.getDefaultValue() : key.getType().cast(val);
    }

    @Override
    public <T> T get(ConfigKey<T> key, T defaultValue) {
        return values.containsKey(key.getKey())
                ? key.getType().cast(values.get(key.getKey()))
                : defaultValue;
    }

    @Override
    public <T> void set(ConfigKey<T> key, T value) {
        values.put(key.getKey(), value);
    }

    @Override
    public void reload() {
        System.out.println("Reload configs...");
    }

    @Override
    public void save() {
        System.out.println("Save configs...");
    }

    public void loadFromSource(ConfigSource source, String location) throws Exception {
        Map<String, String> map = source.load(location);
        values.putAll(map);
    }
}
