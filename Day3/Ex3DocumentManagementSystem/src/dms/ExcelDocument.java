package dms;

public class ExcelDocument extends Document {
    public ExcelDocument(String name, String content, Metadata metadata) {
        super(name, content, metadata);
    }

    @Override
    public void read() {
        System.out.println("Reading Excel sheet: " + name);
    }

    @Override
    public void write(String newContent, User byUser) throws SecurityException {
        if (!byUser.hasPermission(this, Permission.WRITE)) throw new SecurityException("No write permission");
        saveNewContent(newContent, "Excel edited by " + byUser.getUsername());
    }

    @Override
    public Document convertTo(DocumentType targetType) {
        return this;
    }

    @Override
    public void compress() {
        System.out.println("Compressing Excel: " + name);
        metadata.addTag("compressed");
    }
}
