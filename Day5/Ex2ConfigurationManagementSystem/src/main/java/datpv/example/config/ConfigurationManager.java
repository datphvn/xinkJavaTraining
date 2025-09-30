package datpv.example.config;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

public class ConfigurationManager {

    // --- Interfaces ---
    public interface ConfigSource {
        Map<String, Object> load() throws ConfigException;
        void watch(ConfigChangeListener listener);
    }

    public interface ConfigChangeListener {
        void onChange(Map<String, Object> newConfig);
    }

    // --- File-based implementation ---
    public static class FileConfigSource implements ConfigSource {
        private final Path filePath;

        public FileConfigSource(Path filePath) {
            this.filePath = filePath;
        }

        @Override
        public Map<String, Object> load() throws ConfigException {
            try {
                String filename = filePath.toString().toLowerCase();
                if (filename.endsWith(".properties")) {
                    Properties props = new Properties();
                    props.load(Files.newBufferedReader(filePath));
                    Map<String, Object> result = new HashMap<>();
                    for (String key : props.stringPropertyNames()) {
                        result.put(key, props.getProperty(key));
                    }
                    return result;
                }
                // TODO: Add YAML, JSON parsing later
                throw new ConfigException("Unsupported file format: " + filename);
            } catch (IOException e) {
                throw new ConfigException("Failed to load config file: " + filePath, e);
            }
        }

        @Override
        public void watch(ConfigChangeListener listener) {
            new Thread(() -> {
                try (WatchService watchService = FileSystems.getDefault().newWatchService()) {
                    Path parentDir = filePath.getParent();
                    if (parentDir == null) return;

                    parentDir.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);

                    while (true) {
                        WatchKey key = watchService.take();
                        for (WatchEvent<?> event : key.pollEvents()) {
                            if (event.context().toString().equals(filePath.getFileName().toString())) {
                                try {
                                    listener.onChange(load());
                                } catch (ConfigException e) {
                                    System.err.println("Reload failed: " + e.getMessage());
                                }
                            }
                        }
                        key.reset();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }, "Config-Watcher").start();
        }
    }

    // --- Configuration wrapper ---
    public static class Configuration {
        private Map<String, Object> values;

        public Configuration(Map<String, Object> initialValues) {
            this.values = new HashMap<>(initialValues);
        }

        public <T> T get(String key, Class<T> type) {
            Object value = values.get(key);
            if (value == null) {
                throw new NoSuchElementException("Missing config key: " + key);
            }
            return type.cast(value);
        }

        public <T> T get(String key, Class<T> type, T defaultValue) {
            Object value = values.get(key);
            return (value == null) ? defaultValue : type.cast(value);
        }

        public void update(Map<String, Object> newValues) {
            this.values = new HashMap<>(newValues);
        }

        public void validate() throws ConfigValidationException {
            // Example: must contain "app.name"
            if (!values.containsKey("app.name")) {
                throw new ConfigValidationException("Missing required config: app.name");
            }
        }
    }

    // --- Exceptions ---
    public static class ConfigException extends Exception {
        public ConfigException(String msg) { super(msg); }
        public ConfigException(String msg, Throwable cause) { super(msg, cause); }
    }

    public static class ConfigValidationException extends Exception {
        public ConfigValidationException(String msg) { super(msg); }
    }
}
