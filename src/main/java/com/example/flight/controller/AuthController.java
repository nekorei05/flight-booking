package com.example.flight.controller;

import com.example.flight.model.AuthResponse;
import com.example.flight.model.LoginRequest;
import com.example.flight.model.RegisterRequest;
import com.example.flight.service.AuthService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public String register(@RequestBody RegisterRequest request) {
        authService.register(request);
        return "User registered successfully";
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request) {
        String token = authService.login(request);
        return new AuthResponse(token);
    }
}