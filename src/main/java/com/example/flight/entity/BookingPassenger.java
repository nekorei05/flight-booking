package com.example.flight.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "booking_passengers")
public class BookingPassenger extends PassengerDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "booking_passenger_seq_gen")
    @SequenceGenerator(
            name = "booking_passenger_seq_gen",
            sequenceName = "booking_passenger_sequence",
            allocationSize = 1
    )
    @Column(name = "booking_passenger_id")
    private Long bookingPassengerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seat_id", nullable = false)
    private FlightSeat seat;
}