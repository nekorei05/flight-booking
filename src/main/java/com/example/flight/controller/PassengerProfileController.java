package com.example.flight.controller;

import com.example.flight.model.PassengerProfileResponse;
import com.example.flight.service.PassengerProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/passenger/profile")
@CrossOrigin(origins = "http://localhost:4200")
public class PassengerProfileController {

    private final PassengerProfileService passengerProfileService;

    public PassengerProfileController(PassengerProfileService passengerProfileService) {
        this.passengerProfileService = passengerProfileService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<PassengerProfileResponse> getProfile(
            @PathVariable Long userId
    ) {
        return ResponseEntity.ok(
                passengerProfileService.getProfile(userId)
        );
    }
}