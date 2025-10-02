public interface ConfigurationSystem {
    <T> T get(ConfigKey<T> key);
    <T> T get(ConfigKey<T> key, T defaultValue);
    <T> void set(ConfigKey<T> key, T value);

    void reload() throws Exception;
    void save() throws Exception;
}
