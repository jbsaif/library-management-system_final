package com.library.system.model;

import jakarta.persistence.*;
import java.time.LocalDate; // SWITCHED to LocalDate
import java.math.BigDecimal; // NEW IMPORT for professional currency handling

@Entity
public class BorrowRecord {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Book book;

    @Column(nullable = false)
    private LocalDate borrowDate = LocalDate.now();

    // NEW FIELD 1: Due date for fine calculation
    @Column(nullable = false)
    private LocalDate dueDate;

    private LocalDate returnDate; // null if not returned

    // NEW FIELD 2: Fine calculated upon return (initializes to 0.00)
    private BigDecimal fineAmount = BigDecimal.ZERO;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Book getBook() { return book; }
    public void setBook(Book book) { this.book = book; }

    public LocalDate getBorrowDate() { return borrowDate; }
    public void setBorrowDate(LocalDate borrowDate) { this.borrowDate = borrowDate; }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    public LocalDate getReturnDate() { return returnDate; }
    public void setReturnDate(LocalDate returnDate) { this.returnDate = returnDate; }

    public BigDecimal getFineAmount() { return fineAmount; }
    public void setFineAmount(BigDecimal fineAmount) { this.fineAmount = fineAmount; }
}