package com.example.flight.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Table(name = "flights")
public class Flight extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "flight_seq_gen")
    @SequenceGenerator(
            name = "flight_seq_gen",
            sequenceName = "flight_sequence",
            allocationSize = 1
    )
    @Column(name = "flight_id")
    private Long flightId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "airline_id", nullable = false)
    private Airline airline;

    @Column(name = "flight_number", nullable = false, length = 10)
    private String flightNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "dep_airport",
            referencedColumnName = "airport_code",
            nullable = false
    )
    private Airport departureAirport;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "arr_airport",
            referencedColumnName = "airport_code",
            nullable = false
    )
    private Airport arrivalAirport;

    @Column(name = "departure_time", nullable = false)
    private LocalDateTime departureTime;

    @Column(name = "arrival_time", nullable = false)
    private LocalDateTime arrivalTime;

    @Column(name = "total_seats", nullable = false)
    private Integer totalSeats;

    @Column(name = "price_per_seat", nullable = false, precision = 10, scale = 2)
    private BigDecimal pricePerSeat;

    @Version
    @Column(nullable = false)
    private Integer version;

    @OneToMany(mappedBy = "flight", fetch = FetchType.LAZY)
    private List<FlightSeat> seats;

    @OneToMany(mappedBy = "flight", fetch = FetchType.LAZY)
    private List<Booking> bookings;
}