package com.example.flight.model.booking;

import com.example.flight.enums.BookingStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class BookingResponse {

    private Long bookingId;

    private Long userId;

    private Long flightId;

    private Integer seatCount;

    private LocalDateTime bookingDate;

    private BookingStatus status;

    private Long paymentId;

    private BigDecimal paymentAmount;

    private List<PassengerResponse> passengers;
}