package dms;

public class VideoDocument extends Document {
    public VideoDocument(String name, String content, Metadata metadata) {
        super(name, content, metadata);
    }

    @Override
    public void read() {
        System.out.println("Playing video preview: " + name);
    }

    @Override
    public void write(String newContent, User byUser) {
        if (!byUser.hasPermission(this, Permission.WRITE)) throw new SecurityException("No write permission");
        saveNewContent(newContent, "Video updated by " + byUser.getUsername());
    }

    @Override
    public Document convertTo(DocumentType targetType) {
        return this;
    }

    @Override
    public void compress() {
        System.out.println("Transcoding & compressing video: " + name);
        metadata.addTag("compressed");
    }
}
