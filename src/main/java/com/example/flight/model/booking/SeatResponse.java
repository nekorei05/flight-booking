package com.example.flight.model.booking;
import com.example.flight.enums.SeatStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SeatResponse {

    private Long seatId;
    private String seatNumber;
    private SeatStatus status;
}