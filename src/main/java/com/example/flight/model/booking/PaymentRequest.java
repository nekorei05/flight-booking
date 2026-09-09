package com.example.flight.model.booking;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PaymentRequest {

    @NotNull(message = "Payment result is required")
    private Boolean success;
}