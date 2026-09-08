package com.example.flight.repository;

import com.example.flight.entity.Airport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AirportRepository extends JpaRepository<Airport, String> {
    boolean existsByAirportCode(String airportCode);
    boolean existsByAirportName(String airportName);

}
