package com.library.system.service;

import com.library.system.model.Book;
import com.library.system.model.BorrowRecord;
import com.library.system.model.User;
import com.library.system.repository.BookRepository;
import com.library.system.repository.BorrowRecordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.math.BigDecimal;
import java.util.List;

@Service
public class BorrowServiceImpl implements BorrowService {

    private final BookRepository bookRepository;
    private final BorrowRecordRepository borrowRecordRepository;
    private final UserService userService;

    // CONSTANTS
    private static final int LOAN_PERIOD_DAYS = 14;
    private static final BigDecimal FINE_RATE_PER_DAY = new BigDecimal("0.50");
    private static final int MAX_ACTIVE_LOANS = 5;

    public BorrowServiceImpl(BookRepository bookRepository,
                             BorrowRecordRepository borrowRecordRepository,
                             UserService userService) {
        this.bookRepository = bookRepository;
        this.borrowRecordRepository = borrowRecordRepository;
        this.userService = userService;
    }

    @Override
    @Transactional
    public BorrowRecord borrowBook(Long bookId, String username) {

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book not found with ID: " + bookId));

        if (book.getAvailableCopies() <= 0) {
            throw new IllegalStateException("Book is currently out of stock.");
        }

        User user = userService.findByUsername(username);
        if (user == null) {
            throw new IllegalArgumentException("User not found: " + username);
        }

        // --- Loan Limit Check (Retained) ---
        if (user.getRole().equals("ROLE_USER")) {
            List<BorrowRecord> activeLoans = findBorrowedBooksByUser(username);
            if (activeLoans.size() >= MAX_ACTIVE_LOANS) {
                throw new IllegalStateException("Loan limit reached. You cannot borrow more than "
                        + MAX_ACTIVE_LOANS + " books.");
            }
        }
        // -------------------------------------

        LocalDate today = LocalDate.now();
        LocalDate dueDate = today.plusDays(LOAN_PERIOD_DAYS);

        // 1. Decrease available copies
        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookRepository.save(book);

        // 2. Create and save the BorrowRecord
        BorrowRecord record = new BorrowRecord();
        record.setBook(book);
        record.setUser(user);
        record.setBorrowDate(today);
        record.setDueDate(dueDate);

        return borrowRecordRepository.save(record);
    }

    @Override
    public List<BorrowRecord> findBorrowedBooksByUser(String username) {
        User user = userService.findByUsername(username);
        if (user == null) {
            throw new IllegalArgumentException("User not found.");
        }
        return borrowRecordRepository.findByUserIdAndReturnDateIsNull(user.getId());
    }

    // NEW METHOD IMPLEMENTATION
    @Override
    public List<BorrowRecord> findAllActiveBorrowRecords() {
        // Fetches all records where returnDate is null
        return borrowRecordRepository.findByReturnDateIsNull();
    }

    @Override
    @Transactional
    public void returnBook(Long recordId) {

        // 1. Get the BorrowRecord
        BorrowRecord record = borrowRecordRepository.findById(recordId)
                .orElseThrow(() -> new IllegalArgumentException("Borrow record not found with ID: " + recordId));

        // 2. Check if already returned
        if (record.getReturnDate() != null) {
            throw new IllegalStateException("Book has already been returned.");
        }

        LocalDate returnDate = LocalDate.now();

        // --- Fine Calculation Logic (Retained) ---
        LocalDate dueDate = record.getDueDate();
        BigDecimal fine = BigDecimal.ZERO;

        if (returnDate.isAfter(dueDate)) {
            long daysOverdue = ChronoUnit.DAYS.between(dueDate, returnDate);
            fine = FINE_RATE_PER_DAY.multiply(BigDecimal.valueOf(daysOverdue));
        }

        record.setFineAmount(fine);
        // -----------------------------------------

        // 3. Update the Book copies
        Book book = record.getBook();
        book.setAvailableCopies(book.getAvailableCopies() + 1);
        bookRepository.save(book);

        // 4. Mark the BorrowRecord as returned
        record.setReturnDate(returnDate);
        borrowRecordRepository.save(record);
    }
}