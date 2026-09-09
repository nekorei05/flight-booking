package com.example.flight.repository;

import com.example.flight.entity.PassengerProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PassengerProfileRepository extends JpaRepository<PassengerProfile, Long> {

    Optional<PassengerProfile> findByUserUserId(Long userId);
}