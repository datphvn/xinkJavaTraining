// Review.java
import java.time.LocalDateTime;

public class Review {
    private final String reviewer;
    private final int rating;
    private final String comment;
    private final LocalDateTime date;

    public Review(String reviewer, int rating, String comment) {
        this.reviewer = reviewer;
        this.rating = rating;
        this.comment = comment;
        this.date = LocalDateTime.now();
    }

    public int getRating() { return rating; }
    public String getComment() { return comment; }
}
