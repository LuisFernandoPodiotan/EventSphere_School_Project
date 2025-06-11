package com.example.set;


import com.example.set.model.User;
import com.example.set.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/verify")
    public String verifyEmail(@RequestParam("token") String token) {
        Optional<User> userOptional = Optional.ofNullable(userRepository.findByVerificationToken(token));

        if (userOptional.isEmpty()) {
            return "Invalid verification token.";
        }

        User user = userOptional.get();

        // Check if the token has expired
        if (user.getTokenExpiryDate().isBefore(LocalDateTime.now())) {
            return "Verification token has expired. Please request a new one.";
        }

        // Verify the user's email
        user.setEmailVerified(true);
        user.setVerificationToken(null); // Remove token after verification
        user.setTokenExpiryDate(null); // Remove expiration date
        userRepository.save(user);

        return "/Login";
    }
}

