package dms;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Simple inverted index mapping token -> set of document IDs.
 */
public class SearchIndex {
    private final Map<String, Set<String>> index = new ConcurrentHashMap<>();

    public void indexDocument(Document doc) {
        // naive tokenization
        String text = (doc.getContent() == null) ? "" : doc.getContent().toLowerCase();
        String[] tokens = text.split("\\W+");
        for (String t : tokens) {
            if (t.isBlank()) continue;
            index.computeIfAbsent(t, k -> ConcurrentHashMap.newKeySet()).add(doc.getId());
        }
    }

    public Set<String> search(String query) {
        String q = query.toLowerCase().trim();
        if (q.isBlank()) return Collections.emptySet();
        return index.getOrDefault(q, Collections.emptySet());
    }
}
