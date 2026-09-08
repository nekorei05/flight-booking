package com.example.flight.model.flight;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FlightResponse {

    private Long flightId;

    private Long airlineId;
    private String airlineCode;
    private String airlineName;

    private String flightNumber;

    private String departureAirportCode;
    private String departureAirportName;

    private String arrivalAirportCode;
    private String arrivalAirportName;

    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;

    private Integer totalSeats;
    private BigDecimal pricePerSeat;
}