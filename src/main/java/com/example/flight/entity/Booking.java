package com.example.flight.entity;
import com.example.flight.enums.BookingStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Table(name = "bookings")
public class Booking extends BaseEntity {



    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "booking_seq_gen")
    @SequenceGenerator(
            name = "booking_seq_gen",
            sequenceName = "booking_sequence",
            allocationSize = 1
    )
    @Column(name = "booking_id")
    private Long bookingId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flight_id", nullable = false)
    private Flight flight;

    @Column(name = "seat_count", nullable = false)
    private Integer seatCount;

    @Column(name = "booking_date")
    private LocalDateTime bookingDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private BookingStatus status;

    @OneToMany(
            mappedBy = "booking",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<BookingPassenger> passengers;

    @OneToMany(mappedBy = "booking", fetch = FetchType.LAZY)
    private List<Payment> payments;
}