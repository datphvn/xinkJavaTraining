package dms;

public class Main {
    public static void main(String[] args) {
        DocumentManager mgr = new DocumentManager();

        Metadata m1 = new Metadata("Alice");
        PdfDocument pdf = new PdfDocument("report.pdf", "This is the quarterly report about sales and revenue.", m1);

        Metadata m2 = new Metadata("Bob");
        WordDocument doc = new WordDocument("notes.docx", "Meeting notes: action items include testing and deployment.", m2);

        mgr.addDocument(pdf);
        mgr.addDocument(doc);

        // Users and permissions
        User alice = new User("alice");
        User bob = new User("bob");
        alice.grant(pdf, Permission.READ);
        alice.grant(pdf, Permission.WRITE);
        bob.grant(doc, Permission.READ);

        // Read polymorphically
        Document[] docs = {pdf, doc};
        for (Document d : docs) d.read(); // polymorphism: run subclass read()

        // Write
        try {
            pdf.write("Updated quarterly report content with new numbers.", alice);
        } catch (SecurityException ex) {
            System.err.println("Write failed: " + ex.getMessage());
        }

        // Convert
        Document converted = mgr.convert(pdf, DocumentType.WORD);
        System.out.println("Converted: " + converted);

        // Compress
        converted.compress();

        // Version control: show history
        pdf.getVersionControl().getHistory().forEach(v -> System.out.println("PDF version: " + v.id + " msg=" + v.message));

        // Search indexing
        mgr.searchByToken("report").forEach(d -> System.out.println("Search hit: " + d));

        // Collaborative editing
        CollaborativeSession session = mgr.openSession(doc);
        try {
            boolean ok = session.tryEdit(bob, "Meeting notes updated by Bob.");
            System.out.println("Bob edit success: " + ok);
        } catch (SecurityException se) {
            System.out.println("Bob cannot edit: " + se.getMessage());
        }
        // grant and try again
        bob.grant(doc, Permission.WRITE);
        boolean ok2 = session.tryEdit(bob, "Meeting notes updated by Bob (attempt 2).");
        System.out.println("Bob edit success 2: " + ok2);
        session.getHistory().forEach(h -> System.out.println("Collab history: " + h));
    }
}
