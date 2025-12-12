package com.library.system.service;

import com.library.system.model.BorrowRecord;

import java.util.List;

public interface BorrowService {

    /**
     * Attempts to borrow a book.
     * @param bookId The ID of the book to borrow.
     * @param username The username of the person borrowing the book.
     * @return The new BorrowRecord.
     * @throws IllegalStateException if the book is not available or loan limit reached.
     */
    BorrowRecord borrowBook(Long bookId, String username);

    /**
     * Finds all books currently borrowed (unreturned) by a single user.
     * @param username The username of the borrower.
     * @return A list of active BorrowRecords for that user.
     */
    List<BorrowRecord> findBorrowedBooksByUser(String username);

    /**
     * NEW METHOD: Finds all books currently borrowed (unreturned) across all users.
     * @return A list of all active BorrowRecords.
     */
    List<BorrowRecord> findAllActiveBorrowRecords();

    /**
     * Handles the book return process.
     * @param recordId The ID of the BorrowRecord to mark as returned.
     * @throws IllegalStateException if the record is already returned.
     */
    void returnBook(Long recordId);
}