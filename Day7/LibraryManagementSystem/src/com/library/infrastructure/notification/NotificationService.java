package com.library.infrastructure.notification;

import com.library.domain.model.Book;
import com.library.domain.model.Member;

public interface NotificationService {
    void sendBorrowingConfirmation(Member member, Book book);
    void sendReturnConfirmation(Member member, Book book);
}
