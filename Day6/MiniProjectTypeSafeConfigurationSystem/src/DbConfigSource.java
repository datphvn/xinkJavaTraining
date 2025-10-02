import java.util.*;

public class DbConfigSource implements ConfigSource {
    @Override
    public Map<String, String> load(String location) throws Exception {
        // Giả lập (thực tế query DB)
        Map<String, String> map = new HashMap<>();
        map.put("db.url", "jdbc:mysql://localhost:3306/app");
        map.put("db.user", "root");
        return map;
    }
}
