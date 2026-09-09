package com.example.flight.model.airline;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AirlineRequest {

    @NotBlank(message = "Airline code is required")
    @Size(min = 2, max = 3, message = "Airline code must be 2-3 characters")
    private String airlineCode;

    @NotBlank(message = "Airline name is required")
    private String airlineName;

}
