package com.example.flight.model.booking;

import com.example.flight.enums.PaymentStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PaymentResponse {

    private Long paymentId;

    private Long bookingId;

    private BigDecimal amount;

    private LocalDateTime paymentDate;

    private PaymentStatus status;

    private String transactionId;
}