package dms;

public class PdfDocument extends Document {
    public PdfDocument(String name, String content, Metadata metadata) {
        super(name, content, metadata);
    }

    @Override
    public void read() {
        System.out.println("Reading PDF: " + name + " content snippet: " + (content.length() > 60 ? content.substring(0,60) + "..." : content));
    }

    @Override
    public void write(String newContent, User byUser) throws SecurityException {
        if (!byUser.hasPermission(this, Permission.WRITE)) throw new SecurityException("No write permission");
        // demonstrate super usage: call helper in base class
        saveNewContent(newContent, "Edited by " + byUser.getUsername());
    }

    @Override
    public Document convertTo(DocumentType targetType) {
        switch (targetType) {
            case WORD:
                // simple conversion: wrap content
                WordDocument w = new WordDocument(name + ".docx", "Converted from PDF:\n" + content, metadata.copy());
                return w;
            case EXCEL:
                return new ExcelDocument(name + ".xlsx", "Converted from PDF:\n" + content, metadata.copy());
            default:
                return this;
        }
    }

    @Override
    public void compress() {
        System.out.println("Compressing PDF " + name + " (simulated).");
        metadata.addTag("compressed");
    }
}
