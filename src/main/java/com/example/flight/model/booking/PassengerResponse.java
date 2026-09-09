package com.example.flight.model.booking;

import com.example.flight.enums.PassengerGender;
import lombok.Data;

@Data
public class PassengerResponse {

    private Long bookingPassengerId;

    private Long seatId;

    private String passengerName;

    private Integer passengerAge;

    private PassengerGender passengerGender;

    private String passportNumber;
}