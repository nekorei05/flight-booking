package com.example.flight.service;

import com.example.flight.entity.Booking;
import com.example.flight.entity.BookingPassenger;
import com.example.flight.entity.FlightSeat;
import com.example.flight.entity.Payment;
import com.example.flight.enums.BookingStatus;
import com.example.flight.enums.PaymentStatus;
import com.example.flight.enums.SeatStatus;
import com.example.flight.exception.BadRequestException;
import com.example.flight.exception.ResourceNotFoundException;
import com.example.flight.model.booking.PaymentRequest;
import com.example.flight.model.booking.PaymentResponse;
import com.example.flight.repository.BookingRepository;
import com.example.flight.repository.FlightSeatRepository;
import com.example.flight.repository.PaymentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;
    private final FlightSeatRepository flightSeatRepository;

    @Transactional
    public PaymentResponse processPayment(
            Long paymentId,
            PaymentRequest request) {

        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Payment not found"));

        if (payment.getStatus() != PaymentStatus.PENDING) {
            throw new BadRequestException(
                    "Payment has already been processed");
        }

        Booking booking = payment.getBooking();

        if (booking == null) {
            throw new BadRequestException(
                    "Payment is not associated with a booking");
        }

        if (booking.getStatus() != BookingStatus.PENDING) {
            throw new BadRequestException(
                    "Booking cannot be paid for in its current status");
        }

        List<FlightSeat> seats = booking.getPassengers()
                .stream()
                .map(BookingPassenger::getSeat)
                .toList();


        for (FlightSeat seat : seats) {

            if (seat.getStatus() != SeatStatus.HELD) {
                throw new BadRequestException(
                        "Seat " + seat.getSeatNumber()
                                + " is no longer reserved");
            }
        }

        payment.setPaymentDate(LocalDateTime.now());

        //payment failure
        if (!request.getSuccess()) {

            payment.setStatus(PaymentStatus.FAILED);

            paymentRepository.save(payment);


            booking.setStatus(BookingStatus.CANCELLED);

            bookingRepository.save(booking);


            seats.forEach(seat ->
                    seat.setStatus(SeatStatus.AVAILABLE)
            );

            flightSeatRepository.saveAll(seats);

            return mapToResponse(payment);
        }

        //payment success
        payment.setStatus(PaymentStatus.SUCCESS);

        payment.setTransactionId(
                "TXN-" + UUID.randomUUID()
        );

        paymentRepository.save(payment);


        booking.setStatus(BookingStatus.CONFIRMED);

        bookingRepository.save(booking);


        seats.forEach(seat ->
                seat.setStatus(SeatStatus.BOOKED)
        );

        flightSeatRepository.saveAll(seats);

        return mapToResponse(payment);
    }

    private PaymentResponse mapToResponse(Payment payment) {

        PaymentResponse response = new PaymentResponse();

        response.setPaymentId(
                payment.getPaymentId());

        response.setBookingId(
                payment.getBooking().getBookingId());

        response.setAmount(
                payment.getAmount());

        response.setPaymentDate(
                payment.getPaymentDate());

        response.setStatus(
                payment.getStatus());

        response.setTransactionId(
                payment.getTransactionId());

        return response;
    }
}