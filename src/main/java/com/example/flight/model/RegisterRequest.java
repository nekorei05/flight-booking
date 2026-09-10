package com.example.flight.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank(message = "Full Name is required")
    private String fullName;

    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 15, message = "Password must be 6-15 characters")
    private String password;

    @NotBlank(message = "Mobile Number is required")
    @Pattern(
            regexp = "^[0-9]{10}$",
            message = "Mobile number must be exactly 10 digits"
    )
    private String mobileNumber;

    @Valid
    @NotNull(message = "Passenger details are required")
    private PassengerProfileRequest passengerProfile;
}