package com.example.set;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.set.model.User;
import com.example.set.repository.UserRepository;
import com.example.set.service.EmailService;
@Controller
public class ForgotpassController{

@Autowired
private UserRepository userRepository;

@Autowired
private EmailService emailService;

@GetMapping("/forgot-password")
	public String forgotPassword() {
	    return "forgot-password";
	}
      
	@PostMapping("/user/forgot-password")
	
	public String processForgotPassword(@RequestParam("email") String email, Model model) {
		User user = userRepository.findByEmail(email);

	   
	    if (user == null) {
	        model.addAttribute("error", "Email not found!");
	        return "forgot-password";
	    }
	   
	    String token = UUID.randomUUID().toString();
	    user.setVerificationToken(token);
	   
	    user.setTokenExpiryDate(LocalDateTime.now().plusHours(1));
	    userRepository.save(user);
	   
	    try {
			
	    	emailService.sendPasswordResetEmail(email, token);
	        model.addAttribute("message", "Password reset link has been sent to your email!");
	    } catch (Exception e) {
	        model.addAttribute("error", "Error sending password reset email!");
	        e.printStackTrace(); // Log the exception
	    }
	   
	    return "forgot-password";
	}
	
	 @GetMapping("/reset-password")
		public String resetpassword() {
		    return "reset-password";
		}
	 
	 @GetMapping("/user/reset-password")
	 public String resetPassword(@RequestParam("token") String token, Model model) {
	     User user = userRepository.findByVerificationToken(token);

	     if (user == null) {
	         model.addAttribute("error", "Invalid reset token!");
	         return "login";
	     }

	     System.out.println("Token: " + token);
	     System.out.println("Expiry Date: " + user.getTokenExpiryDate());
	     System.out.println("Current Time: " + LocalDateTime.now());

	     if (user.getTokenExpiryDate() == null || user.getTokenExpiryDate().isBefore(LocalDateTime.now())) {
	         model.addAttribute("error", "Reset token has expired!");
	         return "login";
	     }

	     model.addAttribute("token", token);
	     return "reset-password";
	 }
	
	 @PostMapping("/user/reset-password")
	 public String processResetPassword(
	         @RequestParam("token") String token,
	         @RequestParam("password") String password,
	         @RequestParam("confirmPassword") String confirmPassword,
	         Model model) {

	     User user = userRepository.findByVerificationToken(token);

	     if (user == null) {
	         model.addAttribute("error", "Invalid reset token!");
	         return "reset-password";
	     }

	     if (user.getTokenExpiryDate() == null || user.getTokenExpiryDate().isBefore(LocalDateTime.now())) {
	         model.addAttribute("error", "Reset token has expired!");
	         return "reset-password";
	     }

	     if (!password.equals(confirmPassword)) {
	         model.addAttribute("error", "Passwords do not match!");
	         model.addAttribute("token", token);
	         return "reset-password";
	     }

	     user.setPassword(password);

	     System.out.println("Updating user: " + user.getEmail());
	     System.out.println("New Password: " + password);

	     user.setVerificationToken(null);
	     user.setTokenExpiryDate(null);

	     userRepository.save(user);

	     model.addAttribute("message", "Password has been reset successfully! You can now login.");
	     return "login";
	 }
}