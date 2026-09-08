package com.example.flight.service;
import com.example.flight.entity.Airline;
import com.example.flight.exception.ResourceNotFoundException;
import com.example.flight.model.airline.AirlineRequest;
import com.example.flight.model.airline.AirlineResponse;
import com.example.flight.repository.AirlineRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class AirlineService {

    private final AirlineRepository airlineRepository;

    public AirlineService(AirlineRepository airlineRepository) {
        this.airlineRepository = airlineRepository;
    }

    public AirlineResponse createAirline(AirlineRequest airlineRequest) {
        Airline airline = new Airline();
        airline.setAirlineCode(airlineRequest.getAirlineCode());
        airline.setAirlineName(airlineRequest.getAirlineName());
        airline.setLogoPath(airlineRequest.getLogoPath());

        Airline savedAirline = airlineRepository.save(airline);

        return new AirlineResponse(
                savedAirline.getAirlineId(),
                savedAirline.getAirlineCode(),
                savedAirline.getAirlineName(),
                savedAirline.getLogoPath()
        );
    }

    public List<AirlineResponse> getAirlines(){

        List<Airline> airlineList = airlineRepository.findAll();
        List<AirlineResponse> airlineResponses= new ArrayList<>();


        for (Airline airL : airlineList) {
            airlineResponses.add(new AirlineResponse(
                    airL.getAirlineId(),
                    airL.getAirlineCode(),
                    airL.getAirlineName(),
                    airL.getLogoPath()
            ));
        }
        return airlineResponses;

    }

    public AirlineResponse getAirlineById(Long id){

        Airline airline = airlineRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Airline not found"));
            return new AirlineResponse(
                    airline.getAirlineId(),
                    airline.getAirlineCode(),
                    airline.getAirlineName(),
                    airline.getLogoPath()
            );
    }

    public AirlineResponse updateAirline(Long id, AirlineRequest airlineRequest){

        Airline airline = airlineRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Airline not found"));

        airline.setAirlineCode(airlineRequest.getAirlineCode());
        airline.setAirlineName(airlineRequest.getAirlineName());
        airline.setLogoPath(airlineRequest.getLogoPath());

        Airline updatedAirline = airlineRepository.save(airline);

        return new AirlineResponse(
                updatedAirline.getAirlineId(),
                updatedAirline.getAirlineCode(),
                updatedAirline.getAirlineName(),
                updatedAirline.getLogoPath()
        );
    }

    public String deleteAirline(Long id){
        Airline airline=airlineRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Airline not found"));

        airlineRepository.delete(airline);

        return "Airline with Id: " + id + " successfully deleted";
    }
}
