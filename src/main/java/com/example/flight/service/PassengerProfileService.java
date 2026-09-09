package com.example.flight.service;

import com.example.flight.entity.PassengerProfile;
import com.example.flight.exception.AuthException;
import com.example.flight.model.PassengerProfileResponse;
import com.example.flight.repository.PassengerProfileRepository;
import org.springframework.stereotype.Service;

@Service
public class PassengerProfileService {

    private final PassengerProfileRepository passengerProfileRepository;

    public PassengerProfileService(PassengerProfileRepository passengerProfileRepository) {
        this.passengerProfileRepository = passengerProfileRepository;
    }

    public PassengerProfileResponse getProfile(Long userId) {

        PassengerProfile profile = passengerProfileRepository
                .findByUserUserId(userId)
                .orElseThrow(() ->
                        new AuthException("Passenger profile not found", 404)
                );

        return new PassengerProfileResponse(
                profile.getPassengerProfileId(),
                profile.getUser().getUserId(),
                profile.getPassengerName(),
                profile.getPassengerAge(),
                profile.getPassengerGender(),
                profile.getPassportNumber()
        );
    }
}