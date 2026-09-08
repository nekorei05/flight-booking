package com.example.flight.controller;

import com.example.flight.model.airline.AirlineRequest;
import com.example.flight.model.airline.AirlineResponse;
import com.example.flight.service.AirlineService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/airlines")
@CrossOrigin(origins = {
        "http://localhost:4200",
        "https://*.ngrok-free.app",
        "https://*.ngrok-free.dev"
})
public class AirlineController {

    private final AirlineService airlineService;

    public AirlineController(AirlineService airlineService) {
        this.airlineService = airlineService;
    }

    @PostMapping("/create")
    public ResponseEntity<AirlineResponse> createAirline(@Valid @RequestBody AirlineRequest airlineRequest){
        AirlineResponse airlineResponse = airlineService.createAirline(airlineRequest);

        return ResponseEntity.ok(airlineResponse);
    }

    @GetMapping
    public ResponseEntity<List<AirlineResponse>> getAirlines(){
        List<AirlineResponse> airlineResponseList = airlineService.getAirlines();

        return ResponseEntity.ok(airlineResponseList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AirlineResponse> getAirlineById(@Valid @PathVariable Long id){
        AirlineResponse airlineResponse = airlineService.getAirlineById(id);

        return ResponseEntity.ok(airlineResponse);
    }
    @PutMapping("/{id}")
    public ResponseEntity<AirlineResponse> updateAirline(@PathVariable Long id, @Valid @RequestBody AirlineRequest airlineRequest){
        AirlineResponse airlineResponse = airlineService.updateAirline(id,airlineRequest);

        return ResponseEntity.ok(airlineResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteAirline(@Valid @PathVariable Long id){
        String resultMessage = airlineService.deleteAirline(id);

        return ResponseEntity.ok(Map.of("message", resultMessage));
    }
}
