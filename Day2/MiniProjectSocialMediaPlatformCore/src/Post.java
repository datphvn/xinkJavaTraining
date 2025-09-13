import java.util.*;

public class Post {
    private final String postId;
    private final User author;
    private String content;
    private Set<User> likes = new HashSet<>();
    private List<Comment> comments = new ArrayList<>();

    public Post(String postId, User author, String content) {
        this.postId = postId;
        this.author = author;
        this.content = content;
    }

    public String getPostId() { return postId; }
    public User getAuthor() { return author; }

    public boolean addLike(User u) {
        return likes.add(u);
    }

    public void addComment(Comment c) {
        comments.add(c);
    }

    @Override
    public String toString() {
        return "[" + postId + "] " + author.getName() + ": " + content +
                " | Likes: " + likes.size() + " | Comments: " + comments.size();
    }
}
