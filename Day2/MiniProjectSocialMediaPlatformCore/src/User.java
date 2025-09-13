import java.util.*;

public class User {
    private final String userId;
    private final String name;
    private Set<User> friends = new HashSet<>();
    private List<Post> posts = new ArrayList<>();
    private List<String> notifications = new ArrayList<>();

    public User(String userId, String name) {
        this.userId = userId;
        this.name = name;
    }

    public String getUserId() { return userId; }
    public String getName() { return name; }

    public void addFriend(User u) { friends.add(u); }
    public Set<User> getFriends() { return friends; }

    public void addPost(Post p) { posts.add(p); }
    public List<Post> getPosts() { return posts; }

    public void addNotification(String msg) { notifications.add(msg); }
    public List<String> getNotifications() { return notifications; }
    public void clearNotifications() { notifications.clear(); }
}
