package dms;

import java.util.*;

public class DocumentManager {
    private final Map<String, Document> storage = new HashMap<>();
    private final SearchIndex index = new SearchIndex();
    private final Map<String, CollaborativeSession> sessions = new HashMap<>();

    public void addDocument(Document d) {
        storage.put(d.getId(), d);
        index.indexDocument(d);
    }

    public Optional<Document> getById(String id) {
        return Optional.ofNullable(storage.get(id));
    }

    public Set<Document> searchByToken(String token) {
        Set<String> ids = index.search(token);
        Set<Document> r = new HashSet<>();
        for (String id : ids) {
            Document d = storage.get(id);
            if (d != null) r.add(d);
        }
        return r;
    }

    public CollaborativeSession openSession(Document d) {
        return sessions.computeIfAbsent(d.getId(), k -> new CollaborativeSession(d));
    }

    // utility: convert doc and register new doc
    public Document convert(Document doc, DocumentType toType) {
        Document converted = doc.convertTo(toType);
        addDocument(converted);
        return converted;
    }
}
