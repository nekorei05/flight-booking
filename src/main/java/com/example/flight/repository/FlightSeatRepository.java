package com.example.flight.repository;

import com.example.flight.entity.FlightSeat;
import com.example.flight.enums.SeatStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface FlightSeatRepository extends JpaRepository<FlightSeat, Long> {

    List<FlightSeat> findByFlightFlightId(Long flightId);

    List<FlightSeat> findByFlightFlightIdAndStatus(
            Long flightId,
            SeatStatus status
    );

    List<FlightSeat> findByFlightFlightIdAndSeatIdIn(
            Long flightId,
            List<Long> seatIds
    );

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            SELECT s
            FROM FlightSeat s
            WHERE s.flight.flightId = :flightId
            AND s.seatId IN :seatIds
            """)
    List<FlightSeat> findSeatsForUpdate(
            @Param("flightId") Long flightId,
            @Param("seatIds") List<Long> seatIds
    );
}