package com.example.flight.service;

import com.example.flight.entity.Airline;
import com.example.flight.entity.Airport;
import com.example.flight.entity.Flight;
import com.example.flight.entity.FlightSeat;
import com.example.flight.enums.SeatStatus;
import com.example.flight.exception.BadRequestException;
import com.example.flight.exception.ResourceNotFoundException;
import com.example.flight.model.flight.FlightRequest;
import com.example.flight.model.flight.FlightResponse;
import com.example.flight.repository.AirlineRepository;
import com.example.flight.repository.AirportRepository;
import com.example.flight.repository.FlightRepository;
import com.example.flight.repository.FlightSeatRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class FlightService {

    private final FlightRepository flightRepository;
    private final FlightSeatRepository flightSeatRepository;
    private final AirlineRepository airlineRepository;
    private final AirportRepository airportRepository;

    public FlightService(
            FlightRepository flightRepository,
            FlightSeatRepository flightSeatRepository,
            AirlineRepository airlineRepository,
            AirportRepository airportRepository) {

        this.flightRepository = flightRepository;
        this.flightSeatRepository = flightSeatRepository;
        this.airlineRepository = airlineRepository;
        this.airportRepository = airportRepository;
    }

    public FlightResponse createFlight(FlightRequest request) {

        Airline airline = airlineRepository.findById(request.getAirlineId())
                .orElseThrow(() -> new ResourceNotFoundException("Airline not found"));

        Airport departureAirport = airportRepository.findById(
                request.getDepartureAirportCode()
        ).orElseThrow(() -> new ResourceNotFoundException("Departure airport not found"));

        Airport arrivalAirport = airportRepository.findById(
                request.getArrivalAirportCode()
        ).orElseThrow(() -> new ResourceNotFoundException("Arrival airport not found"));

        if (departureAirport.getAirportCode()
                .equals(arrivalAirport.getAirportCode())) {
            throw new BadRequestException(
                    "Departure and arrival airports cannot be the same"
            );
        }

        if (!request.getArrivalTime().isAfter(request.getDepartureTime())) {
            throw new BadRequestException(
                    "Arrival time must be after departure time"
            );
        }

        Flight flight = new Flight();

        flight.setAirline(airline);
        flight.setFlightNumber(request.getFlightNumber());
        flight.setDepartureAirport(departureAirport);
        flight.setArrivalAirport(arrivalAirport);
        flight.setDepartureTime(request.getDepartureTime());
        flight.setArrivalTime(request.getArrivalTime());
        flight.setTotalSeats(request.getTotalSeats());
        flight.setPricePerSeat(request.getPricePerSeat());

        Flight savedFlight = flightRepository.save(flight);

        createSeats(savedFlight);

        return mapToResponse(savedFlight);
    }

    private void createSeats(Flight flight) {

        List<FlightSeat> seats = new ArrayList<>();

        for (int i = 1; i <= flight.getTotalSeats(); i++) {

            FlightSeat seat = new FlightSeat();

            seat.setFlight(flight);
            seat.setSeatNumber(String.valueOf(i));
            seat.setStatus(SeatStatus.AVAILABLE);

            seats.add(seat);
        }

        flightSeatRepository.saveAll(seats);
    }

    public List<FlightResponse> getFlights() {

        List<Flight> flights = flightRepository.findAll();

        List<FlightResponse> responses = new ArrayList<>();

        for (Flight flight : flights) {
            responses.add(mapToResponse(flight));
        }

        return responses;
    }

    public FlightResponse getFlightById(Long id) {

        Flight flight = flightRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Flight not found"));

        return mapToResponse(flight);
    }

    public FlightResponse updateFlight(Long id, FlightRequest request) {

        Flight flight = flightRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Flight not found"));

        Airline airline = airlineRepository.findById(request.getAirlineId())
                .orElseThrow(() -> new ResourceNotFoundException("Airline not found"));

        Airport departureAirport = airportRepository.findById(
                request.getDepartureAirportCode()
        ).orElseThrow(() -> new ResourceNotFoundException("Departure airport not found"));

        Airport arrivalAirport = airportRepository.findById(
                request.getArrivalAirportCode()
        ).orElseThrow(() -> new ResourceNotFoundException("Arrival airport not found"));

        if (departureAirport.getAirportCode()
                .equals(arrivalAirport.getAirportCode())) {
            throw new BadRequestException(
                    "Departure and arrival airports cannot be the same"
            );
        }

        if (!request.getArrivalTime().isAfter(request.getDepartureTime())) {
            throw new BadRequestException(
                    "Arrival time must be after departure time"
            );
        }

        flight.setAirline(airline);
        flight.setFlightNumber(request.getFlightNumber());
        flight.setDepartureAirport(departureAirport);
        flight.setArrivalAirport(arrivalAirport);
        flight.setDepartureTime(request.getDepartureTime());
        flight.setArrivalTime(request.getArrivalTime());
        flight.setTotalSeats(request.getTotalSeats());
        flight.setPricePerSeat(request.getPricePerSeat());

        Flight updatedFlight = flightRepository.save(flight);

        return mapToResponse(updatedFlight);
    }

    public String deleteFlight(Long id) {

        Flight flight = flightRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Flight not found"));

        flightRepository.delete(flight);

        return "Flight with Id: " + id + " successfully deleted";
    }

    private FlightResponse mapToResponse(Flight flight) {

        return FlightResponse.builder()
                .flightId(flight.getFlightId())
                .airlineId(flight.getAirline().getAirlineId())
                .airlineCode(flight.getAirline().getAirlineCode())
                .airlineName(flight.getAirline().getAirlineName())
                .flightNumber(flight.getFlightNumber())
                .departureAirportCode(
                        flight.getDepartureAirport().getAirportCode()
                )
                .departureAirportName(
                        flight.getDepartureAirport().getAirportName()
                )
                .arrivalAirportCode(
                        flight.getArrivalAirport().getAirportCode()
                )
                .arrivalAirportName(
                        flight.getArrivalAirport().getAirportName()
                )
                .departureTime(flight.getDepartureTime())
                .arrivalTime(flight.getArrivalTime())
                .totalSeats(flight.getTotalSeats())
                .pricePerSeat(flight.getPricePerSeat())
                .build();
    }
    public List<FlightResponse> searchFlights(
            String departure,
            String arrival,
            LocalDate date) {

        String departureCode = departure.trim().toUpperCase();
        String arrivalCode = arrival.trim().toUpperCase();

        Airport departureAirport = airportRepository.findById(departureCode)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Departure airport not found"));

        Airport arrivalAirport = airportRepository.findById(arrivalCode)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Arrival airport not found"));

        if (departureAirport.getAirportCode()
                .equals(arrivalAirport.getAirportCode())) {

            throw new BadRequestException(
                    "Departure and arrival airports cannot be the same"
            );
        }

        if (date.isBefore(LocalDate.now())) {
            throw new BadRequestException(
                    "Flight date cannot be in the past"
            );
        }

        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.plusDays(1).atStartOfDay();

        List<Flight> flights = flightRepository.searchFlights(
                departureCode,
                arrivalCode,
                startOfDay,
                endOfDay
        );

        List<FlightResponse> responses = new ArrayList<>();

        for (Flight flight : flights) {
            responses.add(mapToResponse(flight));
        }

        return responses;
    }

}