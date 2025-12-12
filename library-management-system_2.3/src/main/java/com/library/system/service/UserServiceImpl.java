package com.library.system.service;

import com.library.system.model.User;
import com.library.system.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List; // NEW IMPORT

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // NEW METHOD IMPLEMENTATION
    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    // NEW METHOD IMPLEMENTATION
    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    // NEW METHOD IMPLEMENTATION - used by the AdminController for updates
    @Override
    public void save(User user) {
        userRepository.save(user);
    }
}