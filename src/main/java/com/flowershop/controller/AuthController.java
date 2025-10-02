package com.flowershop.controller;

import com.flowershop.dto.AuthRequest;
import com.flowershop.dto.AuthResponse;
import com.flowershop.model.User;
import com.flowershop.service.UserService;
import com.flowershop.config.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody User user) {
        try {
            User registeredUser = userService.registerUser(user);
            String token = jwtUtil.generateToken(registeredUser.getUsername());
            return ResponseEntity.ok(new AuthResponse(token, registeredUser.getUsername(), "Registration successful", true));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new AuthResponse(null, null, e.getMessage(), false));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authRequest) {
        try {
            User user = userService.authenticateUser(authRequest.getUsername(), authRequest.getPassword());
            String token = jwtUtil.generateToken(user.getUsername());
            return ResponseEntity.ok(new AuthResponse(token, user.getUsername(), "Login successful", true));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new AuthResponse(null, null, e.getMessage(), false));
        }
    }
}