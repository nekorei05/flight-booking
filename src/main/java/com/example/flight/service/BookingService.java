package com.example.flight.service;

import com.example.flight.entity.Booking;
import com.example.flight.entity.BookingPassenger;
import com.example.flight.entity.Flight;
import com.example.flight.entity.FlightSeat;
import com.example.flight.entity.Payment;
import com.example.flight.entity.User;
import com.example.flight.enums.BookingStatus;
import com.example.flight.enums.PaymentStatus;
import com.example.flight.enums.SeatStatus;
import com.example.flight.exception.BadRequestException;
import com.example.flight.exception.ResourceNotFoundException;
import com.example.flight.model.booking.BookingRequest;
import com.example.flight.model.booking.BookingResponse;
import com.example.flight.model.booking.PassengerRequest;
import com.example.flight.model.booking.PassengerResponse;
import com.example.flight.repository.BookingPassengerRepository;
import com.example.flight.repository.BookingRepository;
import com.example.flight.repository.FlightRepository;
import com.example.flight.repository.FlightSeatRepository;
import com.example.flight.repository.PaymentRepository;
import com.example.flight.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final BookingPassengerRepository bookingPassengerRepository;
    private final PaymentRepository paymentRepository;
    private final FlightRepository flightRepository;
    private final FlightSeatRepository flightSeatRepository;
    private final UserRepository userRepository;

    @Transactional
    public BookingResponse createBooking(BookingRequest request) {

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        Flight flight = flightRepository.findById(request.getFlightId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Flight not found"));

        List<PassengerRequest> passengers = request.getPassengers();

        if (passengers == null || passengers.isEmpty()) {
            throw new BadRequestException(
                    "At least one passenger is required");
        }

        List<Long> seatIds = passengers.stream()
                .map(PassengerRequest::getSeatId)
                .toList();

        if (seatIds.size() != seatIds.stream().distinct().count()) {
            throw new BadRequestException(
                    "Duplicate seats are not allowed");
        }

        // Lock selected seats during this transaction.
        List<FlightSeat> seats =
                flightSeatRepository.findSeatsForUpdate(
                        flight.getFlightId(),
                        seatIds
                );

        if (seats.size() != seatIds.size()) {
            throw new BadRequestException(
                    "One or more selected seats do not belong to this flight");
        }


        for (FlightSeat seat : seats) {

            if (seat.getStatus() != SeatStatus.AVAILABLE) {
                throw new BadRequestException(
                        "Seat " + seat.getSeatNumber()
                                + " is not available");
            }
        }

        //hold seats while booking waiting
        seats.forEach(seat ->
                seat.setStatus(SeatStatus.HELD)
        );

        flightSeatRepository.saveAll(seats);

        // Create booking.
        Booking booking = new Booking();

        booking.setUser(user);
        booking.setFlight(flight);
        booking.setSeatCount(passengers.size());
        booking.setBookingDate(LocalDateTime.now());
        booking.setStatus(BookingStatus.PENDING);
        booking.setPassengers(new ArrayList<>());

        booking = bookingRepository.save(booking);


        for (PassengerRequest passengerRequest : passengers) {

            FlightSeat selectedSeat = seats.stream()
                    .filter(seat ->
                            seat.getSeatId()
                                    .equals(passengerRequest.getSeatId()))
                    .findFirst()
                    .orElseThrow(() ->
                            new BadRequestException(
                                    "Invalid seat selected"));

            BookingPassenger bookingPassenger =
                    new BookingPassenger();

            bookingPassenger.setBooking(booking);
            bookingPassenger.setSeat(selectedSeat);
            bookingPassenger.setPassengerName(
                    passengerRequest.getPassengerName());
            bookingPassenger.setPassengerAge(
                    passengerRequest.getPassengerAge());
            bookingPassenger.setPassengerGender(
                    passengerRequest.getPassengerGender());
            bookingPassenger.setPassportNumber(
                    passengerRequest.getPassportNumber());

            bookingPassengerRepository.save(bookingPassenger);

            booking.getPassengers().add(bookingPassenger);
        }


        BigDecimal amount = flight.getPricePerSeat()
                .multiply(
                        BigDecimal.valueOf(passengers.size())
                );


        Payment payment = new Payment();

        payment.setBooking(booking);
        payment.setAmount(amount);
        payment.setPaymentDate(LocalDateTime.now());
        payment.setStatus(PaymentStatus.PENDING);

        paymentRepository.save(payment);

        return mapToResponse(booking, payment);
    }

    public BookingResponse getBookingById(Long bookingId) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Booking not found"));

        Payment payment = paymentRepository
                .findByBooking(booking)
                .orElse(null);

        return mapToResponse(booking, payment);
    }

    public List<BookingResponse> getBookingsByUser(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"));

        return bookingRepository.findByUser(user)
                .stream()
                .map(booking -> {

                    Payment payment = paymentRepository
                            .findByBooking(booking)
                            .orElse(null);

                    return mapToResponse(booking, payment);
                })
                .toList();
    }

    @Transactional
    public BookingResponse cancelBooking(Long bookingId) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Booking not found"));

        if (booking.getStatus() != BookingStatus.CONFIRMED) {
            throw new BadRequestException(
                    "Only confirmed bookings can be cancelled");
        }

        // Release seats
        List<BookingPassenger> passengers =
                booking.getPassengers();

        if (passengers != null) {

            List<FlightSeat> seats = passengers.stream()
                    .map(BookingPassenger::getSeat)
                    .toList();

            seats.forEach(seat ->
                    seat.setStatus(SeatStatus.AVAILABLE)
            );

            flightSeatRepository.saveAll(seats);
        }

        // Refund
        Payment payment = paymentRepository
                .findByBooking(booking)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Payment not found"));

        if (payment.getStatus() != PaymentStatus.SUCCESS) {
            throw new BadRequestException(
                    "Payment cannot be refunded");
        }

        payment.setStatus(PaymentStatus.REFUNDED);
        payment.setPaymentDate(LocalDateTime.now());

        paymentRepository.save(payment);

        // Cancel booking
        booking.setStatus(BookingStatus.CANCELLED);

        bookingRepository.save(booking);

        return mapToResponse(booking, payment);
    }

    private BookingResponse mapToResponse(
            Booking booking,
            Payment payment) {

        BookingResponse response = new BookingResponse();

        response.setBookingId(booking.getBookingId());
        response.setUserId(booking.getUser().getUserId());
        response.setFlightId(booking.getFlight().getFlightId());
        response.setSeatCount(booking.getSeatCount());
        response.setBookingDate(booking.getBookingDate());
        response.setStatus(booking.getStatus());

        if (payment != null) {
            response.setPaymentId(payment.getPaymentId());
            response.setPaymentAmount(payment.getAmount());
        }

        List<PassengerResponse> passengers =
                booking.getPassengers() == null
                        ? List.of()
                        : booking.getPassengers()
                        .stream()
                        .map(this::mapPassengerToResponse)
                        .toList();

        response.setPassengers(passengers);

        return response;
    }

    private PassengerResponse mapPassengerToResponse(
            BookingPassenger passenger) {

        PassengerResponse response = new PassengerResponse();

        response.setBookingPassengerId(
                passenger.getBookingPassengerId());

        response.setSeatId(
                passenger.getSeat().getSeatId());

        response.setPassengerName(
                passenger.getPassengerName());

        response.setPassengerAge(
                passenger.getPassengerAge());

        response.setPassengerGender(
                passenger.getPassengerGender());

        response.setPassportNumber(
                passenger.getPassportNumber());

        return response;
    }
}