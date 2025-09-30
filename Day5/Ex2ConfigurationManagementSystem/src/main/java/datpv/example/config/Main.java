package datpv.example.config;

import java.nio.file.Path;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws Exception {
        Path configFile = Path.of("src/main/resources/app.properties");

        ConfigurationManager.FileConfigSource fileSource =
                new ConfigurationManager.FileConfigSource(configFile);

        Map<String, Object> initialConfig = fileSource.load();
        ConfigurationManager.Configuration config =
                new ConfigurationManager.Configuration(initialConfig);

        config.validate();
        System.out.println("App name: " + config.get("app.name", String.class));
        System.out.println("DB user: " + config.get("db.user", String.class));

        // Watch for changes
        fileSource.watch(config::update);
    }
}
