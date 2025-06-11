package com.example.set.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.set.model.User;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    
    // Find a user by username
    User findByUsername(String username);

    // Find a user by email
    User findByEmail(String email);

    // Find a user by email and password
    User findByEmailAndPassword(String email, String password);

    // Find a user by verification token
    User findByVerificationToken(String token);

    // Retrieve a user by their ID
    Optional<User> findById(Long userId);
}
