package com.example.flight.repository;

import com.example.flight.entity.Airline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface AirlineRepository extends JpaRepository<Airline, Long>{
    boolean existsByAirlineCode(String airlineCode);
    boolean existsByAirlineName(String airlineName);

    Optional<Airline> findByAirlineCode(String airlineCode);
}
