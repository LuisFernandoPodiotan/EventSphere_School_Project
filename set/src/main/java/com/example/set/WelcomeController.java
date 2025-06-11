package com.example.set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.example.set.model.User;
import com.example.set.service.UserService;

import javax.servlet.http.HttpSession;

@Controller
public class WelcomeController {

    @Autowired
    private UserService userService;
    
  
    @GetMapping("/user/register")
    public String showRegisterPage() {
        return "register"; 
    }
    
    @GetMapping("/register")
    public String showregisterPage() {
        return "register"; 
    }
    @GetMapping("/Login")
    public String showLoginPage() {
        return "Login"; 
    }
    @GetMapping("/Dashboard")
    public String Dashboard() {
        return "Dashboard"; 
    }
  
    
    @PostMapping("/Login")
    public String loginUser(@RequestParam String email, @RequestParam String password, 
                          Model model, HttpSession session) {
        User user = userService.findByEmailAndPassword(email, password);

        if (user == null) {
            model.addAttribute("error", "Invalid email or password!");
            return "Login";
        }

        if (!user.isEmailVerified()) {
            model.addAttribute("error", "Please verify your email before logging in.");
            return "Login";
        }
        
        // Store user email in session
        session.setAttribute("userEmail", user.getEmail());
        
        return "redirect:/Dashboard";
    }


    @GetMapping("/user/new")
    public String showRegisterPage(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("error", "");  // Set default value
        model.addAttribute("message", "");
        return "Login"; // Ensure Login.html exists in `templates/`
    }

    @PostMapping("/user/new")
    public String registerUser(@ModelAttribute User user, Model model) {
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            model.addAttribute("error", "Username is required!");
            return "register";
        }

        userService.registerUser(user);
        model.addAttribute("message", "User registered successfully!");
        model.addAttribute("username", user.getUsername());
        model.addAttribute("email", user.getEmail());
        return "Login"; // Ensure Login.html exists
    }
    
    @PostMapping("/user/login")
    public String loginUser(@ModelAttribute User user, Model model) {
        User existingUser = userService.findByEmailAndPassword(user.getEmail(), user.getPassword());

        if (existingUser == null) {
            model.addAttribute("message", "<script>alert('Invalid email or password!');</script>");
            return "Login"; 
        }

        return "redirect:/Dashboard"; 
    }	


    @GetMapping("/api/users/current")
    @ResponseBody
    public ResponseEntity<?> getCurrentUser(HttpSession session) {
        // For now, we'll use email from session since that's what you store on login
        String email = (String) session.getAttribute("userEmail");
        if (email == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        User user = userService.findByEmail(email);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        
        return ResponseEntity.ok(user);
    }
}
