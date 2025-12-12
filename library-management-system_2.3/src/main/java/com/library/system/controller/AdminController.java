package com.library.system.controller;

import com.library.system.model.User;
import com.library.system.service.BorrowService;
import com.library.system.service.UserService; // NEW IMPORT
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*; // NEW IMPORT for @PathVariable and @ModelAttribute
import java.time.LocalDate;

@Controller
@RequestMapping("/admin")
// Ensure only users with ROLE_ADMIN can access any endpoint in this controller
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final BorrowService borrowService;
    private final UserService userService; // NEW FIELD

    public AdminController(BorrowService borrowService, UserService userService) {
        this.borrowService = borrowService;
        this.userService = userService;
    }

    // --- Loan Report Endpoint (RETAINED) ---
    @GetMapping("/loans")
    public String viewActiveLoans(Model model) {
        model.addAttribute("activeLoans", borrowService.findAllActiveBorrowRecords());
        model.addAttribute("today", LocalDate.now());
        model.addAttribute("title", "Active Loans Report");
        return "admin/loans";
    }

    // --- NEW: List All Users Endpoint ---
    @GetMapping("/users")
    public String listUsers(Model model) {
        model.addAttribute("users", userService.findAll());
        model.addAttribute("title", "Manage Library Users");
        // Maps to src/main/resources/templates/admin/users.html
        return "admin/users";
    }

    // --- NEW: Show Edit User Form Endpoint ---
    @GetMapping("/users/edit/{id}")
    public String editUserForm(@PathVariable Long id, Model model) {
        User user = userService.findById(id);
        if (user == null) {
            // Handle case where user is not found
            return "redirect:/admin/users?error=User not found";
        }
        model.addAttribute("user", user);
        model.addAttribute("title", "Edit User: " + user.getUsername());
        // Maps to src/main/resources/templates/admin/user-edit.html
        return "admin/user-edit";
    }

    // --- NEW: Process Edit User Form Submission ---
    @PostMapping("/users/edit")
    public String updateUser(@ModelAttribute User user, Model model) {
        // Find the existing user to preserve password and other immutable fields
        User existingUser = userService.findById(user.getId());
        if (existingUser == null) {
            return "redirect:/admin/users?error=User not found for update";
        }

        // We only allow the admin to update the role via this form.
        // Other fields (username, password, name) are left as they were.
        existingUser.setRole(user.getRole());

        userService.save(existingUser);
        return "redirect:/admin/users?success=User role updated successfully";
    }
}