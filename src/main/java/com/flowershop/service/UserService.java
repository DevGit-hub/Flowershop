package com.flowershop.service;

import com.flowershop.model.User;
import com.flowershop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registerUser(User user) {
        // Validate input
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            throw new RuntimeException("Username is required");
        }
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            throw new RuntimeException("Email is required");
        }
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            throw new RuntimeException("Password is required");
        }
        if (user.getFullName() == null || user.getFullName().trim().isEmpty()) {
            throw new RuntimeException("Full name is required");
        }

        // Check if username already exists
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        // Check if email already exists
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        // Encode password and set creation date
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCreatedAt(LocalDateTime.now());

        return userRepository.save(user);
    }

    public User authenticateUser(String username, String password) {
        // Validate input
        if (username == null || username.trim().isEmpty()) {
            throw new RuntimeException("Username is required");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new RuntimeException("Password is required");
        }

        // Find user by username
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Check password
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        return user;
    }
}