package com.example.flight.model;

import com.example.flight.enums.PassengerGender;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PassengerProfileResponse {

    private Long passengerProfileId;
    private Long userId;
    private String passengerName;
    private Integer passengerAge;
    private PassengerGender passengerGender;
    private String passportNumber;
}