package com.example.flight.repository;

import com.example.flight.entity.Booking;
import com.example.flight.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

        List<Booking> findByUser(User user);
}
