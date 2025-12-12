package com.library.system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.library.system.service.BookService;
import com.library.system.service.BorrowService;
import com.library.system.model.Book;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/books")
public class BookController {
    private final BookService bookService;
    private final BorrowService borrowService;

    public BookController(BookService bookService, BorrowService borrowService) {
        this.bookService = bookService;
        this.borrowService = borrowService;
    }

    /**
     * UPDATED: Handles both listing all books (when query is empty) and searching (when query is present).
     * The old /books/search endpoint is now merged here.
     */
    @GetMapping
    public String listBooks(@RequestParam(value = "q", required = false) String query, Model model) {
        List<Book> books;
        String title;

        if (query != null && !query.trim().isEmpty()) {
            // Perform the comprehensive search
            String trimmedQuery = query.trim();
            books = bookService.searchBooks(trimmedQuery);
            title = "Search Results for: '" + trimmedQuery + "'";
            model.addAttribute("query", trimmedQuery); // Pass back for the search box value
        } else {
            // List all books
            books = bookService.findAll();
            title = "All Books";
            model.addAttribute("query", ""); // Ensure query is empty for the search box value
        }

        model.addAttribute("books", books);
        model.addAttribute("title", title);

        return "books";
    }

    // Filter books by category - RETAIN
    @GetMapping("/category/{categoryName}")
    public String listByCategory(@PathVariable("categoryName") String categoryName, Model model) {
        model.addAttribute("books", bookService.findByCategory(categoryName));
        model.addAttribute("title", categoryName + " Books");
        return "books";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("book", new Book());
        model.addAttribute("title", "Add New Book");
        return "books/add";
    }

    @PostMapping("/add")
    public String saveBook(@ModelAttribute Book book) {
        bookService.save(book);
        return "redirect:/books";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("book", bookService.findById(id));
        model.addAttribute("title", "Edit Book");
        return "books/edit";
    }

    @PostMapping("/edit")
    public String updateBook(@ModelAttribute Book book) {
        bookService.update(book);
        return "redirect:/books";
    }

    // Retain BORROW ENDPOINT
    @PostMapping("/borrow/{id}")
    public String borrowBook(@PathVariable Long id, Principal principal, Model model) {
        String username = principal.getName();
        try {
            borrowService.borrowBook(id, username);
            return "redirect:/books";
        } catch (IllegalStateException e) {
            // Redirect with a specific error message if stock is zero or limit is reached
            return "redirect:/books?error=" + e.getMessage();
        } catch (IllegalArgumentException e) {
            // Should not happen if a book ID is valid, but good for robustness
            return "redirect:/books?error=Book not found.";
        }
    }
}