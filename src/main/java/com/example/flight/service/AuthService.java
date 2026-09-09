package com.example.flight.service;

import com.example.flight.entity.PassengerProfile;
import com.example.flight.entity.User;
import com.example.flight.enums.UserRole;
import com.example.flight.exception.AuthException;
import com.example.flight.model.LoginRequest;
import com.example.flight.model.RegisterRequest;
import com.example.flight.repository.PassengerProfileRepository;
import com.example.flight.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PassengerProfileRepository passengerProfileRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(
            UserRepository userRepository,
            PassengerProfileRepository passengerProfileRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.passengerProfileRepository = passengerProfileRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User register(RegisterRequest request) {

        String email = request.getEmail().trim().toLowerCase();

        if (userRepository.existsByEmail(email)) {
            throw new AuthException("Email already registered", 409);
        }

        User user = new User();

        user.setFullName(request.getFullName().trim());
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setMobileNumber(request.getMobileNumber());
        user.setRole(UserRole.PASSENGER);

        userRepository.save(user);

        PassengerProfile profile = new PassengerProfile();

        profile.setUser(user);
        profile.setPassengerName(
                request.getPassengerProfile().getPassengerName().trim()
        );
        profile.setPassengerAge(
                request.getPassengerProfile().getPassengerAge()
        );
        profile.setPassengerGender(
                request.getPassengerProfile().getPassengerGender()
        );
        profile.setPassportNumber(
                request.getPassengerProfile().getPassportNumber().trim()
        );

        passengerProfileRepository.save(profile);

        return user;
    }

    public User login(LoginRequest request) {
        String email = request.getEmail().trim().toLowerCase();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new AuthException("Invalid email or password", 401)
                );

        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword()
        )) {
            throw new AuthException("Invalid email or password", 401);
        }

        return user;
    }
}