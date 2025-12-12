package com.library.system.service;

import com.library.system.model.User;

import java.util.List; // NEW IMPORT

public interface UserService {
    // Finds the User entity by their username
    User findByUsername(String username);

    // NEW: Finds all users in the system
    List<User> findAll();

    // NEW: Finds a user by their ID
    User findById(Long id);

    // NEW: Saves a user (used for updating role/details)
    void save(User user);
}