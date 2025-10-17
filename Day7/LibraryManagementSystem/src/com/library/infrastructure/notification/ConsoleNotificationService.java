package com.library.infrastructure.notification;

import com.library.domain.model.Book;
import com.library.domain.model.Member;

public class ConsoleNotificationService implements NotificationService {

    @Override
    public void sendBorrowingConfirmation(Member member, Book book) {
        System.out.printf("[NOTIFY] %s borrowed '%s'%n", member.getName(), book.getTitle());
    }

    @Override
    public void sendReturnConfirmation(Member member, Book book) {
        System.out.printf("[NOTIFY] %s returned '%s'%n", member.getName(), book.getTitle());
    }
}
