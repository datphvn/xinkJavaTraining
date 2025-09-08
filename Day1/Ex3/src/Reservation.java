import java.time.LocalDateTime;

public class Reservation {
    private final String isbn;
    private final Member member;
    private final LocalDateTime reservedAt;

    public Reservation(String isbn, Member member) {
        this.isbn = isbn;
        this.member = member;
        this.reservedAt = LocalDateTime.now();
    }

    // Getters
    public String getIsbn() { return isbn; }
    public Member getMember() { return member; }
    public LocalDateTime getReservedAt() { return reservedAt; }

    @Override
    public String toString() {
        return "Reservation{" + isbn + ", member=" + member.getMemberId() + ", at=" + reservedAt + "}";
    }
}
