package com.example.springboot.controller;

import com.example.springboot.dto.LoginDTO;
import com.example.springboot.dto.PasswordResetDTO;
import com.example.springboot.dto.UserRegistrationDTO;
import com.example.springboot.model.User;
import com.example.springboot.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRegistrationDTO registrationDTO) {

        User user = authService.registerUser(registrationDTO);
        return ResponseEntity.status(201).body("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {

        Optional<Map<String, Object>> loginResponse = authService.loginWithRole(loginDTO);

        if (loginResponse.isPresent()) {
            return ResponseEntity.ok(loginResponse.get());
        } else {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
    }
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody PasswordResetDTO passwordResetDTO) {
        String response = authService.resetPassword(passwordResetDTO);
        return ResponseEntity.ok(response);
    }
}

