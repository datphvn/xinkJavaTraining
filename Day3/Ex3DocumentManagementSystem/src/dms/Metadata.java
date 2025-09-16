package dms;

import java.util.*;

public class Metadata {
    private String author;
    private Date createdAt;
    private Map<String, String> keyValues = new HashMap<>();
    private Set<String> tags = new HashSet<>();

    public Metadata(String author) {
        this.author = author;
        this.createdAt = new Date();
    }
    public String getAuthor() { return author; }
    public Date getCreatedAt() { return createdAt; }
    public void put(String k, String v) { keyValues.put(k,v); }
    public String get(String k) { return keyValues.get(k); }
    public void addTag(String tag) { tags.add(tag); }
    public Set<String> getTags() { return Collections.unmodifiableSet(tags); }

    public Metadata copy() {
        Metadata m = new Metadata(this.author);
        m.keyValues.putAll(this.keyValues);
        m.tags.addAll(this.tags);
        m.createdAt = new Date(this.createdAt.getTime());
        return m;
    }

    @Override
    public String toString() {
        return "Metadata{" + "author='" + author + '\'' + ", createdAt=" + createdAt + ", tags=" + tags + '}';
    }
}
