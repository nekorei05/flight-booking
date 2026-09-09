package com.example.flight.model;

import com.example.flight.enums.PassengerGender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PassengerProfileRequest {

    @NotBlank(message = "Passenger name is required")
    private String passengerName;

    @NotNull(message = "Passenger age is required")
    private Integer passengerAge;

    @NotNull(message = "Passenger gender is required")
    private PassengerGender passengerGender;

    @NotBlank(message = "Passport number is required")
    private String passportNumber;
}