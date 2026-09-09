package com.example.flight.controller;

import com.example.flight.model.booking.BookingRequest;
import com.example.flight.model.booking.BookingResponse;
import com.example.flight.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<BookingResponse> createBooking(
            @Valid @RequestBody BookingRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(bookingService.createBooking(request));
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<BookingResponse> getBooking(
            @PathVariable Long bookingId) {

        return ResponseEntity.ok(
                bookingService.getBookingById(bookingId)
        );
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BookingResponse>> getUserBookings(
            @PathVariable Long userId) {

        return ResponseEntity.ok(
                bookingService.getBookingsByUser(userId)
        );
    }

    @PutMapping("/{bookingId}/cancel")
    public ResponseEntity<BookingResponse> cancelBooking(
            @PathVariable Long bookingId) {

        return ResponseEntity.ok(
                bookingService.cancelBooking(bookingId)
        );
    }
}