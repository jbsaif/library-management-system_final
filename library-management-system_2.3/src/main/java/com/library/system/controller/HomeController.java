package com.library.system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    /**
     * Handles the root URL ('/') after successful login.
     * FIXED: Redirects the user to the existing /books list page.
     */
    @GetMapping("/")
    public String viewHomePage() {
        return "redirect:/books";
    }
}