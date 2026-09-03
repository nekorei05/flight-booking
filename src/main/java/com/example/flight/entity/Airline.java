package com.example.flight.entity;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "airlines")
public class Airline extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "airline_seq_gen")
    @SequenceGenerator(
            name = "airline_seq_gen",
            sequenceName = "airline_sequence",
            allocationSize = 1
    )
    @Column(name = "airline_id")
    private Long airlineId;

    @Column(name = "airline_code", nullable = false, unique = true, length = 3)
    private String airlineCode;

    @Column(name = "airline_name", nullable = false, unique = true, length = 100)
    private String airlineName;

    @Column(name = "logo_path", length = 255)
    private String logoPath;

    @OneToMany(mappedBy = "airline", fetch = FetchType.LAZY)
    private List<Flight> flights;
}