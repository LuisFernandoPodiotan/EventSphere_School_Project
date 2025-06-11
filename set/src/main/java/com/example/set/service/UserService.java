package com.example.set.service;



import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.set.model.User;
import com.example.set.repository.UserRepository;

@Service
public class UserService {
    
    @Autowired
    private UserRepository repo;
    @Autowired
    private EmailService emailService;

    public User registerUser(User user) {
    	String token = UUID.randomUUID().toString();
        user.setVerificationToken(token);
        user.setTokenExpiryDate(LocalDateTime.now().plusHours(24)); // Token expires in 24 hours
        user.setEmailVerified(false); // Ensure the email is unverified initially
        
        User savedUser = repo.save(user);

        // Send verification email
        emailService.sendVerificationEmail(user.getEmail(), token);

        return savedUser;
    }

    public  User findByEmail (String email) {
    	return repo.findByEmail(email);
    }
 
    public User findByEmailAndPassword(String email, String password) {
        return repo.findByEmailAndPassword(email, password);


    }
}
