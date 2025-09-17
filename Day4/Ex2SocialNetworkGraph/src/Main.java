public class Main {
    public static void main(String[] args) {
        SocialNetwork sn = new SocialNetwork();

        // Add users
        sn.addUser("alice", "Alice");
        sn.addUser("bob", "Bob");
        sn.addUser("carol", "Carol");
        sn.addUser("dave", "Dave");

        // Connections
        sn.connect("alice", "bob");
        sn.connect("bob", "carol");
        sn.connect("carol", "dave");

        // Posts
        sn.addPost("bob", "Hello world!");
        sn.addPost("carol", "Java collections are powerful.");
        sn.addPost("dave", "SkipList > TreeMap!");

        // News feed
        System.out.println("Alice's feed: " + sn.getNewsFeed("alice"));

        // Shortest path
        System.out.println("Path Alice -> Dave: " + sn.findShortestPath("alice", "dave"));

        // Recommendations
        System.out.println("Friends recommended for Alice: " + sn.recommendFriends("alice"));

        // Influencers
        System.out.println("Top influencers: " + sn.findInfluencers(2));

        // Communities
        System.out.println("Communities: " + sn.detectCommunities());
    }
}
