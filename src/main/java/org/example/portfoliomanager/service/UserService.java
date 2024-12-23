package org.example.portfoliomanager.service;


import org.example.portfoliomanager.models.User;
import org.example.portfoliomanager.repositories.UserRepository;
import org.springframework.stereotype.Service;
@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null.");
        }
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty.");
        }
        if (userRepository.findByUsername(user.getUsername()) != null) {
            throw new RuntimeException("Username already exists.");
        }
        try {
            return userRepository.save(user);
        } catch (Exception e) {
            throw new RuntimeException("Failed to save the user: " + e.getMessage(), e);
        }
    }

    public User getUserById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("User ID cannot be null.");
        }
        try {
            return userRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve the user by ID: " + e.getMessage(), e);
        }
    }

    public User getUserByUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty.");
        }
        try {
            User user = userRepository.findByUsername(username);
            if (user == null) {
                throw new RuntimeException("User not found with username: " + username);
            }
            return user;
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve the user by username: " + e.getMessage(), e);
        }
    }
}

