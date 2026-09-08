package com.example.flight.controller;

import com.example.flight.entity.User;
import com.example.flight.model.AuthResponse;
import com.example.flight.model.LoginRequest;
import com.example.flight.model.RegisterRequest;
import com.example.flight.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = {
        "http://localhost:4200",
        "https://*.ngrok-free.app",
        "https://*.ngrok-free.dev"
})

public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        User user = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(user, "User registered successfully"));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        User user = authService.login(request);
        return ResponseEntity.ok(toResponse(user, "Login successful"));
    }

    private AuthResponse toResponse(User user, String message) {
        return new AuthResponse(user.getUserId(), user.getFullName(), user.getEmail(), user.getRole(), message);
    }
}