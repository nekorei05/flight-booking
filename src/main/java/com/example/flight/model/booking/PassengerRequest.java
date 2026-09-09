package com.example.flight.model.booking;

import com.example.flight.enums.PassengerGender;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PassengerRequest {

    @NotNull(message = "Seat id is required")
    private Long seatId;

    @NotBlank(message = "Passenger name is required")
    @Size(max = 40, message = "Passenger name must not exceed 40 characters")
    private String passengerName;

    @NotNull(message = "Passenger age is required")
    @Min(value = 1, message = "Passenger age must be greater than 0")
    private Integer passengerAge;

    @NotNull(message = "Passenger gender is required")
    private PassengerGender passengerGender;

    @NotBlank(message = "Passport number is required")
    @Size(max = 20, message = "Passport number must not exceed 20 characters")
    private String passportNumber;
}