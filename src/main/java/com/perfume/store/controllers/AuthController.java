package com.perfume.store.controllers;

import com.perfume.store.models.User;
import com.perfume.store.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Show login page
    @GetMapping("/login")
    public String showLoginPage() {
        return "auth/login";
    }

    // Show register page
    @GetMapping("/register")
    public String showRegisterPage() {
        return "auth/register";
    }

    // Handle register form
    @PostMapping("/register")
    public String registerUser(@RequestParam String fullName,
                               @RequestParam String email,
                               @RequestParam String password,
                               @RequestParam(required = false) String phone,
                               @RequestParam(required = false) String address,
                               Model model) {

        // Check if email already exists
        if (userRepository.findByEmail(email) != null) {
            model.addAttribute("error", "Email already registered!");
            return "auth/register";
        }

        // Create new user
        User user = new User();
        user.setFullName(fullName);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setPhone(phone);
        user.setAddress(address);
        user.setRole("CUSTOMER");

        userRepository.save(user);

        return "redirect:/auth/login";
    }
}
