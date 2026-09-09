package com.example.flight.repository;

import com.example.flight.entity.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {

    @Query("""
        SELECT f FROM Flight f
        WHERE f.departureAirport.airportCode = :departure
        AND f.arrivalAirport.airportCode = :arrival
        AND f.departureTime >= :startOfDay
        AND f.departureTime < :endOfDay
    """)
    List<Flight> searchFlights(
            @Param("departure") String departure,
            @Param("arrival") String arrival,
            @Param("startOfDay") LocalDateTime startOfDay,
            @Param("endOfDay") LocalDateTime endOfDay
    );
}