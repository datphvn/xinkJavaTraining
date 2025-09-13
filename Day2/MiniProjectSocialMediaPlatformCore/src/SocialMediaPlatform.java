import java.util.*;

public class SocialMediaPlatform {
    private Map<String, User> users = new HashMap<>();
    private Map<String, Post> posts = new HashMap<>();
    private int userCounter = 1;
    private int postCounter = 1;

    // Đăng ký user mới
    public User registerUser(String name) {
        String userId = "U" + userCounter++;
        User u = new User(userId, name);
        users.put(userId, u);
        return u;
    }

    // Xem tất cả user
    public void viewAllUsers() {
        if (users.isEmpty()) {
            System.out.println("No users registered.");
            return;
        }
        System.out.println("=== All Users ===");
        for (User u : users.values()) {
            System.out.println(u.getUserId() + " - " + u.getName());
        }
    }

    // Tạo post
    public void createPost(String userId, String content) {
        User author = users.get(userId);
        if (author == null) {
            System.out.println("User not found!");
            return;
        }
        String postId = "P" + postCounter++;
        Post p = new Post(postId, author, content);
        posts.put(postId, p);
        author.addPost(p);
        System.out.println("Post created with ID: " + postId);
    }

    // Thêm bạn
    public void addFriend(String userId1, String userId2) {
        User u1 = users.get(userId1);
        User u2 = users.get(userId2);
        if (u1 == null || u2 == null) {
            System.out.println("User not found!");
            return;
        }
        u1.addFriend(u2);
        u2.addFriend(u1);
        System.out.println(u1.getName() + " and " + u2.getName() + " are now friends.");
    }

    // Hiển thị news feed
    public void showNewsFeed(String userId) {
        User u = users.get(userId);
        if (u == null) {
            System.out.println("User not found!");
            return;
        }
        System.out.println("=== News Feed for " + u.getName() + " ===");
        for (User friend : u.getFriends()) {
            for (Post p : friend.getPosts()) {
                System.out.println(p);
            }
        }
    }

    // Like post
    public void likePost(String userId, String postId) {
        User u = users.get(userId);
        Post p = posts.get(postId);
        if (u == null || p == null) {
            System.out.println("User or Post not found!");
            return;
        }
        if (p.addLike(u)) {
            p.getAuthor().addNotification(u.getName() + " liked your post " + postId);
            System.out.println(u.getName() + " liked post " + postId);
        } else {
            System.out.println("Already liked.");
        }
    }

    // Comment post
    public void commentPost(String userId, String postId, String content) {
        User u = users.get(userId);
        Post p = posts.get(postId);
        if (u == null || p == null) {
            System.out.println("User or Post not found!");
            return;
        }
        p.addComment(new Comment(u, content));
        p.getAuthor().addNotification(u.getName() + " commented on your post " + postId);
        System.out.println(u.getName() + " commented on post " + postId);
    }

    // Xem notification
    public void viewNotifications(String userId) {
        User u = users.get(userId);
        if (u == null) {
            System.out.println("User not found!");
            return;
        }
        System.out.println("=== Notifications for " + u.getName() + " ===");
        for (String n : u.getNotifications()) {
            System.out.println("- " + n);
        }
        u.clearNotifications();
    }
}
