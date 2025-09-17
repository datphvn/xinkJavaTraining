import java.util.*;
import java.util.stream.Collectors;

public class SocialNetwork {
    private final Map<String, User> users = new HashMap<>();
    private final Map<String, Set<String>> connections = new HashMap<>();
    private final Map<String, List<Post>> userPosts = new HashMap<>();

    // ---- User management ----
    public void addUser(String username, String name) {
        users.putIfAbsent(username, new User(username, name));
        connections.putIfAbsent(username, new HashSet<>());
        userPosts.putIfAbsent(username, new ArrayList<>());
    }

    public void removeUser(String username) {
        users.remove(username);
        connections.remove(username);
        userPosts.remove(username);
        for (Set<String> friends : connections.values()) {
            friends.remove(username);
        }
    }

    // ---- Friend connections ----
    public void connect(String u1, String u2) {
        if (users.containsKey(u1) && users.containsKey(u2)) {
            connections.get(u1).add(u2);
            connections.get(u2).add(u1);
        }
    }

    public void disconnect(String u1, String u2) {
        if (connections.containsKey(u1)) connections.get(u1).remove(u2);
        if (connections.containsKey(u2)) connections.get(u2).remove(u1);
    }

    // ---- Posting ----
    public void addPost(String username, String content) {
        if (users.containsKey(username)) {
            userPosts.get(username).add(new Post(username, content));
        }
    }

    public List<Post> getNewsFeed(String username) {
        if (!users.containsKey(username)) return List.of();
        Set<String> friends = connections.get(username);
        List<Post> feed = new ArrayList<>();
        for (String f : friends) {
            feed.addAll(userPosts.getOrDefault(f, List.of()));
        }
        feed.sort(Comparator.comparing(Post::getTimestamp).reversed());
        return feed;
    }

    // ---- Graph algorithms ----
    // BFS shortest path
    public List<String> findShortestPath(String start, String end) {
        if (!users.containsKey(start) || !users.containsKey(end)) return List.of();
        Queue<List<String>> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();
        queue.add(List.of(start));
        visited.add(start);

        while (!queue.isEmpty()) {
            List<String> path = queue.poll();
            String last = path.get(path.size() - 1);
            if (last.equals(end)) return path;
            for (String neighbor : connections.getOrDefault(last, Set.of())) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    List<String> newPath = new ArrayList<>(path);
                    newPath.add(neighbor);
                    queue.add(newPath);
                }
            }
        }
        return List.of();
    }

    // Recommend friends (friends of friends)
    public Set<String> recommendFriends(String username) {
        if (!users.containsKey(username)) return Set.of();
        Set<String> direct = connections.get(username);
        Set<String> recs = new HashSet<>();
        for (String f : direct) {
            for (String fof : connections.get(f)) {
                if (!fof.equals(username) && !direct.contains(fof)) {
                    recs.add(fof);
                }
            }
        }
        return recs;
    }

    // Influencers (highest degree centrality)
    public List<String> findInfluencers(int topN) {
        return connections.entrySet().stream()
                .sorted((a, b) -> Integer.compare(b.getValue().size(), a.getValue().size()))
                .limit(topN)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    // Community detection using BFS (connected components)
    public List<Set<String>> detectCommunities() {
        List<Set<String>> communities = new ArrayList<>();
        Set<String> visited = new HashSet<>();

        for (String user : users.keySet()) {
            if (!visited.contains(user)) {
                Set<String> community = new HashSet<>();
                Queue<String> queue = new LinkedList<>();
                queue.add(user);
                visited.add(user);
                while (!queue.isEmpty()) {
                    String u = queue.poll();
                    community.add(u);
                    for (String neighbor : connections.getOrDefault(u, Set.of())) {
                        if (!visited.contains(neighbor)) {
                            visited.add(neighbor);
                            queue.add(neighbor);
                        }
                    }
                }
                communities.add(community);
            }
        }
        return communities;
    }
}
