package com.event.mastery.event;

// Sự kiện khi người dùng được tạo
public class UserCreatedEvent extends Event {
    private final String userId;
    private final String email;

    public UserCreatedEvent(String source, String userId, String email) {
        super(source);
        this.userId = userId;
        this.email = email;
    }

    public String getUserId() { return userId; }
    public String getEmail() { return email; }

    @Override
    public String toString() {
        return super.toString() + String.format(" | User ID: %s, Email: %s", userId, email);
    }
}
