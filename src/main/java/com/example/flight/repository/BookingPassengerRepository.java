package com.example.flight.repository;


import com.example.flight.entity.BookingPassenger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface BookingPassengerRepository extends JpaRepository<BookingPassenger, Long> {

}
