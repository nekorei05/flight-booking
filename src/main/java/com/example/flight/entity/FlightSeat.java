package com.example.flight.entity;

import com.example.flight.enums.SeatStatus;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(
        name = "flight_seats",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"flight_id", "seat_number"}
                )
        }
)
public class FlightSeat extends BaseEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seat_seq_gen")
    @SequenceGenerator(
            name = "seat_seq_gen",
            sequenceName = "seat_sequence",
            allocationSize = 1
    )
    @Column(name = "seat_id")
    private Long seatId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flight_id", nullable = false)
    private Flight flight;

    @Column(name = "seat_number", nullable = false, length = 5)
    private String seatNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SeatStatus status;
}