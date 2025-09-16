package dms;

public class WordDocument extends Document {
    public WordDocument(String name, String content, Metadata metadata) {
        super(name, content, metadata);
    }

    @Override
    public void read() {
        System.out.println("Reading Word: " + name + " [" + metadata.getAuthor() + "]");
    }

    @Override
    public void write(String newContent, User byUser) throws SecurityException {
        if (!byUser.hasPermission(this, Permission.WRITE)) throw new SecurityException("No write permission");
        saveNewContent(newContent, "Word edited by " + byUser.getUsername());
    }

    @Override
    public Document convertTo(DocumentType targetType) {
        if (targetType == DocumentType.PDF) {
            PdfDocument p = new PdfDocument(name.replaceAll("\\.docx?$",".pdf"), "PDF version of:\n" + content, metadata.copy());
            return p;
        }
        return this;
    }

    @Override
    public void compress() {
        System.out.println("Compressing Word doc: " + name);
        metadata.addTag("compressed");
    }
}
