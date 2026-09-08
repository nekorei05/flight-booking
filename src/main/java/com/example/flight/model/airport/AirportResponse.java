package com.example.flight.model.airport;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AirportResponse {

    private String airportCode;
    private String airportName;
    private String city;
    private String country;
}