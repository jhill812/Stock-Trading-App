package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    // User Signup
    @PostMapping("/signup")
    public String signup(@RequestBody User user) {
    	 BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    	    user.setPassword(encoder.encode(user.getPassword())); // Hash the password

    	    if (userRepository.findByUsername(user.getUsername()) != null) {
    	        return "Username already exists!";
    	    }

    	    userRepository.save(user);
    	    return "User registered successfully!";
    }

    // User Login
    @PostMapping("/login")
    public String login(@RequestBody User user) {
        User existingUser = userRepository.findByUsername(user.getUsername());
        if (existingUser == null) {
            return "Invalid username or password!";
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (!encoder.matches(user.getPassword(), existingUser.getPassword())) {
            return "Invalid username or password!";
        }

        return "Login successful!";
    }
    
}
