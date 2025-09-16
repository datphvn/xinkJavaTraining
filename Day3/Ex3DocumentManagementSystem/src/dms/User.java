package dms;

import java.util.*;

public class User {
    private final String username;
    private final Map<String, Set<Permission>> acl = new HashMap<>(); // docId -> permissions

    public User(String username) {
        this.username = username;
    }

    public String getUsername() { return username; }

    public void grant(Document doc, Permission p) {
        acl.computeIfAbsent(doc.getId(), k -> new HashSet<>()).add(p);
    }

    public boolean hasPermission(Document doc, Permission p) {
        return acl.getOrDefault(doc.getId(), Collections.emptySet()).contains(p);
    }
}
