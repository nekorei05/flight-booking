package com.example.flight.entity;

import com.example.flight.enums.PassengerGender;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;

@MappedSuperclass
@Data
public abstract class PassengerDetails extends BaseEntity {

    @Column(name = "passenger_name", nullable = false, length = 100)
    private String passengerName;

    @Column(name = "passenger_age", nullable = false)
    private Integer passengerAge;

    @Enumerated(EnumType.STRING)
    @Column(name = "passenger_gender", nullable = false, length = 20)
    private PassengerGender passengerGender;

    @Column(name = "passport_number", nullable = false, length = 20)
    private String passportNumber;
}