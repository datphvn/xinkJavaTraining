public class ConfigKey<T> {
    private final String key;
    private final Class<T> type;
    private final T defaultValue;

    private ConfigKey(String key, Class<T> type, T defaultValue) {
        this.key = key;
        this.type = type;
        this.defaultValue = defaultValue;
    }

    public static <T> ConfigKey<T> of(String key, Class<T> type) {
        return new ConfigKey<>(key, type, null);
    }

    public static <T> ConfigKey<T> of(String key, Class<T> type, T defaultValue) {
        return new ConfigKey<>(key, type, defaultValue);
    }

    public String getKey() { return key; }
    public Class<T> getType() { return type; }
    public T getDefaultValue() { return defaultValue; }
}
