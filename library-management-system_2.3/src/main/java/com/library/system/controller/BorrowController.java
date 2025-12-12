package com.library.system.controller;

import com.library.system.service.BorrowService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
// Only regular users should access their borrow history
@PreAuthorize("hasRole('USER')")
public class BorrowController {

    private final BorrowService borrowService;

    public BorrowController(BorrowService borrowService) {
        this.borrowService = borrowService;
    }

    // Endpoint linked to the "My Borrows" navbar link
    @GetMapping("/borrowed")
    public String listBorrowedBooks(Principal principal, Model model) {
        String username = principal.getName();
        model.addAttribute("borrowRecords", borrowService.findBorrowedBooksByUser(username));
        return "borrowed"; // Maps to borrowed.html
    }

    // Endpoint to handle the book return submission
    @PostMapping("/return/{recordId}")
    public String returnBook(@PathVariable Long recordId, Principal principal) {
        try {
            // We can optionally add an authorization check here to ensure the user is returning their OWN book.
            // For now, we trust the service layer's logic.
            borrowService.returnBook(recordId);
            return "redirect:/borrowed?success=returned";
        } catch (Exception e) {
            // Redirect with an error parameter
            return "redirect:/borrowed?error=" + e.getMessage();
        }
    }
}