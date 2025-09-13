import java.util.*;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static SocialMediaPlatform platform = new SocialMediaPlatform();

    public static void main(String[] args) {
        int choice;
        do {
            System.out.println("\n=== SOCIAL MEDIA MENU ===");
            System.out.println("1. Register user");
            System.out.println("2. View all users");
            System.out.println("3. Create a post");
            System.out.println("4. Add friend");
            System.out.println("5. Show news feed");
            System.out.println("6. Like a post");
            System.out.println("7. Comment on a post");
            System.out.println("8. View notifications");
            System.out.println("0. Exit");
            System.out.print("Choose: ");
            choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1 -> registerUser();
                case 2 -> platform.viewAllUsers();
                case 3 -> createPost();
                case 4 -> addFriend();
                case 5 -> showNewsFeed();
                case 6 -> likePost();
                case 7 -> commentPost();
                case 8 -> viewNotifications();
                case 0 -> System.out.println("Bye!");
                default -> System.out.println("Invalid choice.");
            }
        } while (choice != 0);
    }

    private static void registerUser() {
        System.out.print("Enter user name: ");
        String name = scanner.nextLine();
        User u = platform.registerUser(name);
        System.out.println("Registered user with ID: " + u.getUserId());
    }

    private static void createPost() {
        System.out.print("Enter your userId: ");
        String userId = scanner.nextLine();
        System.out.print("Enter post content: ");
        String content = scanner.nextLine();
        platform.createPost(userId, content);
    }

    private static void addFriend() {
        System.out.print("Enter your userId: ");
        String uid1 = scanner.nextLine();
        System.out.print("Enter friend userId: ");
        String uid2 = scanner.nextLine();
        platform.addFriend(uid1, uid2);
    }

    private static void showNewsFeed() {
        System.out.print("Enter your userId: ");
        String uid = scanner.nextLine();
        platform.showNewsFeed(uid);
    }

    private static void likePost() {
        System.out.print("Enter your userId: ");
        String uid = scanner.nextLine();
        System.out.print("Enter postId: ");
        String pid = scanner.nextLine();
        platform.likePost(uid, pid);
    }

    private static void commentPost() {
        System.out.print("Enter your userId: ");
        String uid = scanner.nextLine();
        System.out.print("Enter postId: ");
        String pid = scanner.nextLine();
        System.out.print("Enter comment: ");
        String comment = scanner.nextLine();
        platform.commentPost(uid, pid, comment);
    }

    private static void viewNotifications() {
        System.out.print("Enter your userId: ");
        String uid = scanner.nextLine();
        platform.viewNotifications(uid);
    }
}
