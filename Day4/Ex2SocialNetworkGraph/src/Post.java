import java.time.LocalDateTime;

public class Post {
    private final String author;
    private final String content;
    private final LocalDateTime timestamp;

    public Post(String author, String content) {
        this.author = author;
        this.content = content;
        this.timestamp = LocalDateTime.now();
    }

    public String getAuthor() { return author; }
    public String getContent() { return content; }
    public LocalDateTime getTimestamp() { return timestamp; }

    @Override
    public String toString() {
        return "[" + timestamp + "] " + author + ": " + content;
    }
}
