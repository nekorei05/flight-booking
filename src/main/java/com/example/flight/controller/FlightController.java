package com.example.flight.controller;

import com.example.flight.model.flight.FlightRequest;
import com.example.flight.model.flight.FlightResponse;
import com.example.flight.service.FlightService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/flights")
@CrossOrigin(origins = "http://localhost:4200")
public class FlightController {

    private final FlightService flightService;

    public FlightController(FlightService flightService) {
        this.flightService = flightService;
    }

    @PostMapping("/create")
    public ResponseEntity<FlightResponse> createFlight(
            @Valid @RequestBody FlightRequest flightRequest) {

        FlightResponse flightResponse =
                flightService.createFlight(flightRequest);

        return ResponseEntity.ok(flightResponse);
    }

    @GetMapping
    public ResponseEntity<List<FlightResponse>> getFlights() {

        List<FlightResponse> flightResponses =
                flightService.getFlights();

        return ResponseEntity.ok(flightResponses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FlightResponse> getFlightById(
            @PathVariable Long id) {

        FlightResponse flightResponse =
                flightService.getFlightById(id);

        return ResponseEntity.ok(flightResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FlightResponse> updateFlight(
            @PathVariable Long id,
            @Valid @RequestBody FlightRequest flightRequest) {

        FlightResponse flightResponse =
                flightService.updateFlight(id, flightRequest);

        return ResponseEntity.ok(flightResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteFlight(
            @PathVariable Long id) {

        String resultMessage =
                flightService.deleteFlight(id);

        return ResponseEntity.ok(Map.of("message", resultMessage));
    }
}