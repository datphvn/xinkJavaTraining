package com.library.infrastructure.persistence;

import com.library.domain.model.Book;
import com.library.domain.model.Member;
import java.util.List;
import java.util.Optional;

public interface BorrowingRepository {
    void recordBorrow(Member member, Book book);
    void recordReturn(Member member, Book book);
    List<Book> findBorrowedBooksByMember(Member member);
    Optional<Member> findBorrowerOfBook(Book book);
}
