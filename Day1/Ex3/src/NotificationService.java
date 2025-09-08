import java.time.LocalDate;

public class NotificationService {
    public void sendDueReminder(Member member, Book book, LocalDate dueDate) {
        System.out.println("[REMINDER] " + member.getName() + ": '" + book.getTitle()
                + "' is due on " + dueDate + ".");
    }

    public void sendOverdueNotice(Member member, Book book, LocalDate dueDate, double fineSoFar) {
        System.out.println("[OVERDUE] " + member.getName() + ": '" + book.getTitle()
                + "' was due on " + dueDate + ". Current fine: " + String.format("%.2f", fineSoFar));
    }

    public void sendReservationReady(Member member, Book book) {
        System.out.println("[RESERVATION READY] " + member.getName() + ": '" + book.getTitle()
                + "' is now available. Please borrow within 2 days.");
    }
}
