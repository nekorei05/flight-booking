package com.example.flight.entity;

import com.example.flight.enums.PassengerGender;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Data
@NoArgsConstructor
@Table(name = "passenger_profiles")
public class PassengerProfile extends PassengerDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "passenger_profile_seq_gen")
    @SequenceGenerator(
            name = "passenger_profile_seq_gen",
            sequenceName = "passenger_profile_sequence",
            allocationSize = 1
    )
    @Column(name = "passenger_profile_id")
    private Long passengerProfileId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;
}