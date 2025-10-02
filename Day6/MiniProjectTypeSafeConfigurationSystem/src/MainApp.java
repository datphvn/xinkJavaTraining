
public class MainApp {
    static class AppConfig {
        @annotations.ConfigNotEmpty
        String dbUrl = "jdbc:mysql://localhost:3306/app";

        @annotations.ConfigRange(min = 1, max = 65535)
        int port = 8080;
    }

    public static void main(String[] args) throws Exception {
        InMemoryConfiguration config = new InMemoryConfiguration();

        // Load từ file properties (giả sử config.properties có key=value)
        config.loadFromSource(new FileConfigSource(), "config.properties");

        // Load từ DB (fake)
        config.loadFromSource(new DbConfigSource(), "db-conn");

        // Validate
        AppConfig appConfig = new AppConfig();
        Validator.validate(appConfig);

        // Dùng Encryption
        String secret = EncryptionService.encrypt("mypassword");
        System.out.println("Encrypted: " + secret);
        System.out.println("Decrypted: " + EncryptionService.decrypt(secret));

        // Audit + Metrics
        AuditLogger.log("Configuration loaded");
        Metrics.record("Config load complete");

        // Web UI
        new WebUiServer().start();

        // Test get config
        ConfigKey<String> urlKey = ConfigKey.of("db.url", String.class);
        System.out.println("DB URL = " + config.get(urlKey));
    }
}
