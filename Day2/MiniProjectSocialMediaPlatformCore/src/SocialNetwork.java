import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.stream.Collectors;

public class SocialNetwork {
    // users registry
    private final Map<String, User> users = new ConcurrentHashMap<>();
    // global recent posts for quick scanning
    private final Deque<Post> globalPosts = new ConcurrentLinkedDeque<>();
    // notifications
    private final NotificationService notificationService = new NotificationService();

    public NotificationService getNotificationService() { return notificationService; }

    // user management
    public User registerUser(String userId, String displayName) {
        User u = new User(userId, new Profile(displayName));
        users.put(userId, u);
        // subscribe user's inbox to notification service (deliver)
        notificationService.subscribe(n -> u.receiveNotification(n));
        return u;
    }

    public User findUser(String userId) { return users.get(userId); }

    // posting
    public Post createPost(String userId, String content, PrivacySettings visibility) {
        User author = users.get(userId);
        if (author == null) throw new IllegalArgumentException("Unknown user");
        Post p = author.createPost(content, visibility);
        globalPosts.addFirst(p);
        // optionally trim globalPosts to limit memory
        if (globalPosts.size() > 10000) globalPosts.removeLast();
        // notify followers about new post (simple)
        for (String followerId : author.getFollowers()) {
            User follower = users.get(followerId);
            if (follower != null) {
                notificationService.notifyAllSubscribers(new Notification(author.getProfile().getDisplayName()+" posted: "+ content));
            }
        }
        return p;
    }

    // like/comment/share
    public void likePost(String likerId, Post p) {
        User liker = users.get(likerId);
        if (liker == null) return;
        boolean added = p.like(liker);
        if (added) {
            notificationService.notifyAllSubscribers(new Notification(liker.getProfile().getDisplayName() + " liked a post by " + p.getAuthor().getProfile().getDisplayName()));
        }
    }

    public void commentPost(String commenterId, Post p, String text) {
        User commenter = users.get(commenterId);
        if (commenter == null) return;
        Comment c = p.comment(commenter, text);
        notificationService.notifyAllSubscribers(new Notification(commenter.getProfile().getDisplayName() + " commented on " + p.getAuthor().getProfile().getDisplayName() + "'s post: " + text));
    }

    public void sharePost(String sharerId, Post p) {
        User sharer = users.get(sharerId);
        if (sharer == null) return;
        p.share();
        notificationService.notifyAllSubscribers(new Notification(sharer.getProfile().getDisplayName() + " shared a post by " + p.getAuthor().getProfile().getDisplayName()));
    }

    // basic feed generation
    public List<Post> getNewsFeed(String userId, int limit) {
        User user = users.get(userId);
        if (user == null) return Collections.emptyList();

        Set<String> allowedSources = new HashSet<>();
        allowedSources.add(userId); // own posts
        allowedSources.addAll(user.getFollowing()); // following
        allowedSources.addAll(user.getFriends()); // friends

        // Strategy: scan recent global posts and pick those visible + from allowed sources or public
        Instant now = Instant.now();
        List<ScoredPost> scored = new ArrayList<>();
        int scanned = 0;
        for (Post p : globalPosts) {
            // limit scanning to keep performance
            if (++scanned > 2000) break;

            // visibility check
            if (!isPostVisibleTo(p, user)) continue;

            // include if from allowed sources or public
            if (allowedSources.contains(p.getAuthor().getUserId()) || p.getVisibility() == PrivacySettings.PUBLIC) {
                // score: recent posts get higher, engagement contributes
                long ageSeconds = Math.max(1, Duration.between(p.getCreatedAt(), now).getSeconds());
                double recencyScore = 1.0 / Math.log(ageSeconds + 2);
                double engagementScore = p.getLikesCount() * 0.1 + p.getComments().size() * 0.2 + p.getShareCount() * 0.3;
                double score = recencyScore + engagementScore;
                scored.add(new ScoredPost(p, score));
            }
        }

        // sort by score desc then time desc
        return scored.stream()
                .sorted(Comparator.comparingDouble(ScoredPost::getScore).reversed()
                        .thenComparing(sp -> sp.getPost().getCreatedAt(), Comparator.reverseOrder()))
                .limit(limit)
                .map(ScoredPost::getPost)
                .collect(Collectors.toList());
    }

    // visibility check logic
    public boolean isPostVisibleTo(Post p, User viewer) {
        PrivacySettings v = p.getVisibility();
        if (v == PrivacySettings.PUBLIC) return true;
        if (v == PrivacySettings.PRIVATE) return p.getAuthor().equals(viewer);
        if (v == PrivacySettings.FRIENDS_ONLY) return p.getAuthor().isFriend(viewer) || p.getAuthor().equals(viewer);
        return false;
    }

    // helper class
    private static class ScoredPost {
        private final Post post;
        private final double score;
        ScoredPost(Post p, double score) { this.post = p; this.score = score; }
        public double getScore() { return score; }
        public Post getPost() { return post; }
    }
}
