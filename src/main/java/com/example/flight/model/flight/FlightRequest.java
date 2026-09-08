package com.example.flight.model.flight;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class FlightRequest {

    @NotNull(message = "Airline ID is required")
    private Long airlineId;

    @NotBlank(message = "Flight number is required")
    private String flightNumber;

    @NotBlank(message = "Departure airport code is required")
    @Size(min = 3, max = 3, message = "Airport code must be 3 characters")
    private String departureAirportCode;

    @NotBlank(message = "Arrival airport code is required")
    @Size(min = 3, max = 3, message = "Airport code must be 3 characters")
    private String arrivalAirportCode;

    @NotNull(message = "Departure time is required")
    private LocalDateTime departureTime;

    @NotNull(message = "Arrival time is required")
    private LocalDateTime arrivalTime;

    @NotNull(message = "Total seats are required")
    @Min(value = 1, message = "Total seats must be at least 1")
    private Integer totalSeats;

    @NotNull(message = "Price per seat is required")
    @DecimalMin(value = "0.0", inclusive = false,
            message = "Price must be greater than 0")
    private BigDecimal pricePerSeat;
}