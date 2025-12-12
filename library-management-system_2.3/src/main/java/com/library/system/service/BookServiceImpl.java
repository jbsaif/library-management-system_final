package com.library.system.service;

import com.library.system.model.Book;
import com.library.system.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository repo;

    public BookServiceImpl(BookRepository repo) {
        this.repo = repo;
    }

    @Override
    public Book save(Book book) {
        // Retain FIX: Initialize availableCopies for new books
        if (book.getId() == null) {
            book.setAvailableCopies(book.getTotalCopies());
        }
        return repo.save(book);
    }

    @Override
    public Book findById(Long id) {
        return repo.findById(id).orElse(null);
    }

    @Override
    public List<Book> findAll() {
        return repo.findAll();
    }

    // UPDATED METHOD IMPLEMENTATION: Calls the comprehensive search
    @Override
    public List<Book> searchBooks(String keyword) {
        return repo.searchBooks(keyword);
    }

    // Existing method for category filtering
    @Override
    public List<Book> findByCategory(String category) {
        return repo.findByCategoryIgnoreCase(category);
    }

    @Override
    public Book update(Book book) {
        return repo.save(book);
    }

    @Override
    public void deleteById(Long id) {
        repo.deleteById(id);
    }
}