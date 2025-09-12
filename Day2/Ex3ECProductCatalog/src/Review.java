

import java.time.LocalDateTime;
import java.util.Objects;

public class Review {
    private final String reviewer;
    private final int rating; // 1..5
    private final String comment;
    private final LocalDateTime createdAt;

    public Review(String reviewer, int rating, String comment) {
        this.reviewer = Objects.requireNonNull(reviewer);
        if (rating < 1 || rating > 5) throw new IllegalArgumentException("rating 1..5");
        this.rating = rating;
        this.comment = comment;
        this.createdAt = LocalDateTime.now();
    }

    public String getReviewer() { return reviewer; }
    public int getRating() { return rating; }
    public String getComment() { return comment; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
