package dms;

import java.util.UUID;

public abstract class Document {
    protected final String id;
    protected String name;
    protected String content; // simplified representation
    protected final Metadata metadata;
    protected final VersionControl versionControl;

    public Document(String name, String content, Metadata metadata) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.content = content;
        this.metadata = metadata;
        this.versionControl = new VersionControl(this);
        // add initial version
        this.versionControl.commit("Initial version");
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getContent() { return content; }
    public Metadata getMetadata() { return metadata; }
    public VersionControl getVersionControl() { return versionControl; }

    // Core operations — subclasses may override as needed
    public abstract void read();
    public abstract void write(String newContent, User byUser) throws SecurityException;
    public abstract Document convertTo(DocumentType targetType);
    public abstract void compress();

    // helper: update content and create version (using composition to VersionControl)
    protected void saveNewContent(String newContent, String message) {
        this.content = newContent;
        this.versionControl.commit(message);
    }

    @Override
    public String toString() {
        return String.format("%s[%s] name='%s'", this.getClass().getSimpleName(), id, name);
    }
}
