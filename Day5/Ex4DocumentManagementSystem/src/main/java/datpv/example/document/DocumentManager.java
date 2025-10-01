package datpv.example.document;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Exercise 4: Document Management System
 * Features:
 * 1. Store documents with metadata
 * 2. Full-text search capabilities
 * 3. Version control for documents
 * 4. Access control and permissions
 * 5. Bulk operations
 * 6. Document conversion
 * 7. Audit logging
 */
public class DocumentManager {

    // ===============================
    // Document Entity
    // ===============================
    public static class Document {
        private final String id;
        private String name;
        private String content;
        private final Map<String, Object> metadata;
        private final List<String> tags;
        private final AccessControl permissions;
        private final List<DocumentVersion> versions;
        private LocalDateTime lastModified;

        public Document(String name, String content) {
            this.id = UUID.randomUUID().toString();
            this.name = name;
            this.content = content;
            this.metadata = new HashMap<>();
            this.tags = new ArrayList<>();
            this.permissions = new AccessControl();
            this.versions = new ArrayList<>();
            this.lastModified = LocalDateTime.now();

            saveVersion("Initial version");
        }

        public String getId() { return id; }
        public String getName() { return name; }
        public String getContent() { return content; }
        public Map<String, Object> getMetadata() { return metadata; }
        public List<String> getTags() { return tags; }
        public AccessControl getPermissions() { return permissions; }
        public List<DocumentVersion> getVersions() { return versions; }

        public void updateContent(String newContent, String comment) {
            this.content = newContent;
            this.lastModified = LocalDateTime.now();
            saveVersion(comment);
        }

        private void saveVersion(String comment) {
            versions.add(new DocumentVersion(versions.size() + 1, content, comment, lastModified));
        }

        @Override
        public String toString() {
            return "Document{id='%s', name='%s', lastModified=%s, tags=%s}"
                    .formatted(id, name, lastModified, tags);
        }
    }

    // ===============================
    // Document Versioning
    // ===============================
    public static class DocumentVersion {
        private final int version;
        private final String content;
        private final String comment;
        private final LocalDateTime timestamp;

        public DocumentVersion(int version, String content, String comment, LocalDateTime timestamp) {
            this.version = version;
            this.content = content;
            this.comment = comment;
            this.timestamp = timestamp;
        }

        @Override
        public String toString() {
            return "Version %d at %s: %s".formatted(version, timestamp, comment);
        }
    }

    // ===============================
    // Access Control
    // ===============================
    public static class AccessControl {
        private final Map<String, Permission> userPermissions = new HashMap<>();

        public void grant(String user, Permission permission) {
            userPermissions.put(user, permission);
        }

        public Permission getPermission(String user) {
            return userPermissions.getOrDefault(user, Permission.READ);
        }
    }

    public enum Permission {
        READ, WRITE, ADMIN
    }

    // ===============================
    // Document Store
    // ===============================
    public static class DocumentStore {
        private final Map<String, Document> documents = new HashMap<>();
        private final List<String> auditLog = new ArrayList<>();

        public void save(Document doc) {
            documents.put(doc.getId(), doc);
            log("Saved document: " + doc.getName());
        }

        public Optional<Document> getById(String id) {
            return Optional.ofNullable(documents.get(id));
        }

        public List<Document> searchByKeyword(String keyword) {
            return documents.values().stream()
                    .filter(d -> d.getContent().contains(keyword) || d.getName().contains(keyword))
                    .toList();
        }

        public void delete(String id) {
            Document removed = documents.remove(id);
            if (removed != null) {
                log("Deleted document: " + removed.getName());
            }
        }

        public void log(String event) {
            String entry = LocalDateTime.now() + " - " + event;
            auditLog.add(entry);
            System.out.println("[AUDIT] " + entry);
        }

        public List<String> getAuditLog() {
            return auditLog;
        }
    }

    // ===============================
    // Demo Main
    // ===============================
    public static void main(String[] args) {
        DocumentStore store = new DocumentStore();

        // Create documents
        Document doc1 = new Document("Report Q1", "This is the Q1 report data.");
        doc1.getTags().add("finance");
        doc1.getMetadata().put("author", "DatPV");

        Document doc2 = new Document("Team Plan", "Our team will work on project X.");
        doc2.getTags().add("planning");
        doc2.getMetadata().put("owner", "Alice");

        // Save documents
        store.save(doc1);
        store.save(doc2);

        // Update document
        doc1.updateContent("Updated Q1 report with final numbers.", "Added final numbers");
        store.save(doc1);

        // Search
        System.out.println("Search results for 'report': " + store.searchByKeyword("report"));

        // Permissions
        doc1.getPermissions().grant("alice", Permission.WRITE);
        doc1.getPermissions().grant("bob", Permission.READ);

        System.out.println("Alice permission: " + doc1.getPermissions().getPermission("alice"));
        System.out.println("Bob permission: " + doc1.getPermissions().getPermission("bob"));

        // Audit trail
        System.out.println("Audit log:");
        store.getAuditLog().forEach(System.out::println);

        // Version history
        System.out.println("Document versions:");
        doc1.getVersions().forEach(System.out::println);
    }
}
