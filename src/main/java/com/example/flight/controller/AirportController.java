package com.example.flight.controller;

import com.example.flight.model.airport.AirportRequest;
import com.example.flight.model.airport.AirportResponse;
import com.example.flight.service.AirportService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/airports")
@CrossOrigin(origins = {
        "http://localhost:4200",
        "https://*.ngrok-free.app",
        "https://*.ngrok-free.dev"
})
public class AirportController {

    private final AirportService airportService;

    public AirportController(AirportService airportService) {
        this.airportService = airportService;
    }

    @PostMapping("/create")
    public ResponseEntity<AirportResponse> createAirport(
            @Valid @RequestBody AirportRequest airportRequest) {

        AirportResponse airportResponse =
                airportService.createAirport(airportRequest);

        return ResponseEntity.ok(airportResponse);
    }

    @GetMapping
    public ResponseEntity<List<AirportResponse>> getAirports() {

        List<AirportResponse> airportResponseList =
                airportService.getAirports();

        return ResponseEntity.ok(airportResponseList);
    }

    @GetMapping("/{airportCode}")
    public ResponseEntity<AirportResponse> getAirportByCode(
            @PathVariable String airportCode) {

        AirportResponse airportResponse =
                airportService.getAirportByCode(airportCode);

        return ResponseEntity.ok(airportResponse);
    }

    @PutMapping("/{airportCode}")
    public ResponseEntity<AirportResponse> updateAirport(
            @PathVariable String airportCode,
            @Valid @RequestBody AirportRequest airportRequest) {

        AirportResponse airportResponse =
                airportService.updateAirport(airportCode, airportRequest);

        return ResponseEntity.ok(airportResponse);
    }

    @DeleteMapping("/{airportCode}")
    public ResponseEntity<Map<String, String>> deleteAirport(
            @PathVariable String airportCode) {

        String resultMessage =
                airportService.deleteAirport(airportCode);

        return ResponseEntity.ok(Map.of("message", resultMessage));
    }
}
