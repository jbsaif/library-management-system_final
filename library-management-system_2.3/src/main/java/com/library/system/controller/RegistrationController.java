package com.library.system.controller;

import com.library.system.model.User;
import com.library.system.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class RegistrationController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public RegistrationController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Show registration form
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    // Process registration form submission
    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user, Model model) {

        // Check if username already exists
        if (userRepository.findByUsername(user.getUsername()) != null) {
            model.addAttribute("errorMessage", "Username already exists.");
            return "register";
        }

        // 1. Encode the password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // 2. Assign default role (User)
        // Note: For simplicity, we are creating a regular user. Admin accounts should be created manually.
        user.setRole("ROLE_USER");

        // 3. Save the new user to the database
        userRepository.save(user);

        // Redirect to login page with a success message
        model.addAttribute("successMessage", "Registration successful! Please log in.");
        return "login";
    }
}