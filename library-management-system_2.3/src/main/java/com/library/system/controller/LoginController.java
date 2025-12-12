package com.library.system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    /**
     * Handles the GET request for the /login URL.
     * This method simply returns the name of the login view template.
     */
    @GetMapping("/login")
    public String showLoginPage() {
        return "login"; // Assumes login.html exists in src/main/resources/templates/
    }
}