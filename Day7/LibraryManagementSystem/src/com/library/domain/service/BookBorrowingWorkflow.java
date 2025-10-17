package com.library.domain.service;

import com.library.domain.exception.LibraryException;
import com.library.domain.model.Book;
import com.library.domain.model.Member;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * Workflow for borrowing a book.
 * Handles the complete process of borrowing a book by a member.
 */
public class BookBorrowingWorkflow implements Workflow {
    private final RuleEngine ruleEngine;

    public BookBorrowingWorkflow(RuleEngine ruleEngine) {
        this.ruleEngine = ruleEngine;
    }

    @Override
    public String getName() {
        return "BOOK_BORROWING";
    }

    @Override
    public String getDescription() {
        return "Workflow for borrowing a book from the library";
    }

    @Override
    public boolean validateContext(WorkflowContext context) {
        return context.containsKey("member") && context.containsKey("book");
    }

    @Override
    public WorkflowResult execute(WorkflowContext context) {
        try {
            // Step 1: Extract and validate inputs
            Member member = context.get("member", Member.class);
            Book book = context.get("book", Book.class);

            if (member == null || book == null) {
                return WorkflowResult.failure("Member or book not found in context");
            }

            // Step 2: Validate member status
            try {
                ruleEngine.executeRules(member);
            } catch (LibraryException e) {
                return WorkflowResult.failure("Member validation failed: " + e.getMessage());
            }

            // Step 3: Validate book availability
            try {
                ruleEngine.executeRules(book);
            } catch (LibraryException e) {
                return WorkflowResult.failure("Book validation failed: " + e.getMessage());
            }

            // Step 4: Check if book can be borrowed
            if (!book.canBorrow()) {
                return WorkflowResult.failure("Book cannot be borrowed: " + book.getTitle());
            }

            // Step 5: Perform borrowing
            try {
                member.borrowBook(book);
                book.borrowCopy();
            } catch (LibraryException e) {
                return WorkflowResult.failure("Borrowing failed: " + e.getMessage());
            }

            // Step 6: Calculate due date
            LocalDate dueDate = calculateDueDate(member, book);

            // Step 7: Prepare result
            Map<String, Object> resultData = new HashMap<>();
            resultData.put("memberId", member.getMemberId());
            resultData.put("bookIsbn", book.getIsbn().getValue());
            resultData.put("bookTitle", book.getTitle());
            resultData.put("borrowDate", LocalDate.now());
            resultData.put("dueDate", dueDate);
            resultData.put("borrowingDays", calculateBorrowingDays(member));

            return WorkflowResult.success(
                "Book '" + book.getTitle() + "' successfully borrowed by member " + member.getMemberId(),
                resultData
            );
        } catch (Exception e) {
            return WorkflowResult.failure("Unexpected error during borrowing: " + e.getMessage(), e);
        }
    }

    private LocalDate calculateDueDate(Member member, Book book) {
        // Base borrowing period: 14 days
        int borrowingDays = 14;

        // Extend for digital books
        if (book.getFormat().isDigital()) {
            borrowingDays = 21;
        }

        return LocalDate.now().plusDays(borrowingDays);
    }

    private int calculateBorrowingDays(Member member) {
        // Default: 14 days
        return 14;
    }
}
