package com.example.flight.controller;

import com.example.flight.model.booking.PaymentRequest;
import com.example.flight.model.booking.PaymentResponse;
import com.example.flight.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")

public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/{paymentId}/process")
    public ResponseEntity<PaymentResponse> processPayment(
            @PathVariable Long paymentId,
            @Valid @RequestBody PaymentRequest request) {

        return ResponseEntity.ok(
                paymentService.processPayment(
                        paymentId,
                        request
                )
        );
    }
}