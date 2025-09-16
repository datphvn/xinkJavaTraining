package dms;

public class ImageDocument extends Document {
    public ImageDocument(String name, String content, Metadata metadata) {
        super(name, content, metadata);
    }

    @Override
    public void read() {
        System.out.println("Displaying image thumbnail for: " + name);
    }

    @Override
    public void write(String newContent, User byUser) {
        // write to image metadata or replace binary; here simplified
        if (!byUser.hasPermission(this, Permission.WRITE)) throw new SecurityException("No write permission");
        saveNewContent(newContent, "Image replaced by " + byUser.getUsername());
    }

    @Override
    public Document convertTo(DocumentType targetType) {
        if (targetType == DocumentType.PDF) {
            return new PdfDocument(name + ".pdf", "PDF with image: " + name, metadata.copy());
        }
        return this;
    }

    @Override
    public void compress() {
        System.out.println("Lossy compressing image: " + name);
        metadata.addTag("compressed");
    }
}
