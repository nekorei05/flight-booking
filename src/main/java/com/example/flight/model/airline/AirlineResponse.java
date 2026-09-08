package com.example.flight.model.airline;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AirlineResponse {
    private Long airlineId;
    private String airlineCode;
    private String airlineName;
    private String logoPath;
}
