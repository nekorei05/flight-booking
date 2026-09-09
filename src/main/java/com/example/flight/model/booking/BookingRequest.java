package com.example.flight.model.booking;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class BookingRequest {

    @NotNull(message = "User id is required")
    private Long userId;

    @NotNull(message = "Flight id is required")
    private Long flightId;

    @NotEmpty(message = "At least one passenger is required")
    @Valid
    private List<PassengerRequest> passengers;
}