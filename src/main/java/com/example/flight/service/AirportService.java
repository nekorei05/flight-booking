package com.example.flight.service;

import com.example.flight.entity.Airport;
import com.example.flight.model.airline.AirlineRequest;
import com.example.flight.model.airline.AirlineResponse;
import com.example.flight.model.airport.AirportRequest;
import com.example.flight.model.airport.AirportResponse;
import com.example.flight.repository.AirportRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AirportService {

    private final AirportRepository airportRepository;

    public AirportService(AirportRepository airportRepository) {
        this.airportRepository = airportRepository;
    }

    public AirportResponse createAirport(AirportRequest airportRequest){
        Airport airport = new Airport();
        airport.setAirportCode(airportRequest.getAirportCode());
        airport.setAirportName(airportRequest.getAirportName());
        airport.setCity(airportRequest.getCity());
        airport.setCountry(airportRequest.getCountry());

        Airport createdAirport = airportRepository.save(airport);

        return new AirportResponse(
                createdAirport.getAirportCode(),
                createdAirport.getAirportName(),
                createdAirport.getCity(),
                createdAirport.getCountry()
        );

    }

    public List<AirportResponse> getAirports() {

        List<Airport> airportList = airportRepository.findAll();
        List<AirportResponse> airportResponses = new ArrayList<>();

        for (Airport airport : airportList) {
            airportResponses.add(new AirportResponse(
                    airport.getAirportCode(),
                    airport.getAirportName(),
                    airport.getCity(),
                    airport.getCountry()
            ));
        }

        return airportResponses;
    }

    public AirportResponse getAirportByCode(String airportCode) {

        Airport airport = airportRepository.findById(airportCode)
                .orElseThrow(() -> new RuntimeException("Airport not found"));

        return new AirportResponse(
                airport.getAirportCode(),
                airport.getAirportName(),
                airport.getCity(),
                airport.getCountry()
        );
    }
    public AirportResponse updateAirport(
            String airportCode,
            AirportRequest airportRequest) {

        Airport airport = airportRepository.findById(airportCode)
                .orElseThrow(() -> new RuntimeException("Airport not found"));

        airport.setAirportName(airportRequest.getAirportName());
        airport.setCity(airportRequest.getCity());
        airport.setCountry(airportRequest.getCountry());

        Airport updatedAirport = airportRepository.save(airport);

        return new AirportResponse(
                updatedAirport.getAirportCode(),
                updatedAirport.getAirportName(),
                updatedAirport.getCity(),
                updatedAirport.getCountry()
        );
    }

    public String deleteAirport(String airportCode) {

        Airport airport = airportRepository.findById(airportCode)
                .orElseThrow(() -> new RuntimeException("Airport not found"));

        airportRepository.delete(airport);

        return "Airport with code: " + airportCode + " successfully deleted";
    }

}
