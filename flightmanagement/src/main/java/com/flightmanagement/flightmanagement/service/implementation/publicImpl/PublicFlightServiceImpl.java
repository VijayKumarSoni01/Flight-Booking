package com.flightmanagement.flightmanagement.service.implementation.publicImpl;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.flightmanagement.flightmanagement.dtos.responseDTOs.BaggagePolicyResDTO;
import com.flightmanagement.flightmanagement.dtos.responseDTOs.PublicFlightResDTO;
import com.flightmanagement.flightmanagement.entity.BaggagePolicy;
import com.flightmanagement.flightmanagement.entity.Flight;
import com.flightmanagement.flightmanagement.mapper.PublicFlightMapper;
import com.flightmanagement.flightmanagement.repository.BaggageRepository;
import com.flightmanagement.flightmanagement.repository.FlightRepository;
import com.flightmanagement.flightmanagement.service.interFace.publicService.PublicFlightService;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class PublicFlightServiceImpl 
        implements PublicFlightService {



    private final FlightRepository flightRepository;

    private final BaggageRepository baggageRepository;

    private final PublicFlightMapper publicFlightMapper;



    @Override
    @Transactional(readOnly = true)
    public List<PublicFlightResDTO> searchFlights(
            String source,
            String destination,
            LocalDate date) {


        source = source.trim();

        destination = destination.trim();



        LocalDateTime startDate =
                date.atStartOfDay();



        LocalDateTime endDate =
                date.plusDays(1)
                    .atStartOfDay();



        return flightRepository
                .searchFlights(
                        source,
                        destination,
                        startDate,
                        endDate
                )
                .stream()
                .map(this::mapFlightWithFare)
                .toList();

    }






    @Override
    @Transactional(readOnly = true)
    public PublicFlightResDTO getFlightDetails(
            Long flightId) {


        Flight flight =
                flightRepository
                .findFlightDetailsById(flightId)
                .orElseThrow(
                        () -> new RuntimeException(
                                "Flight not found")
                );



        PublicFlightResDTO dto =
                mapFlightWithFare(flight);



        List<BaggagePolicy> baggagePolicies =
                baggageRepository
                .findByFlightId(flightId);



        dto.setBaggagePolicies(

                baggagePolicies
                .stream()
                .map(this::mapBaggage)
                .toList()

        );



        return dto;

    }







    private PublicFlightResDTO mapFlightWithFare(
            Flight flight) {


        PublicFlightResDTO dto =
                publicFlightMapper.toDto(flight);



        flight.getFlightFares()
                .forEach(fare -> {



                    switch(fare.getCabinClass()) {


                        case ECONOMY ->

                                dto.setEconomyPrice(
                                        fare.getAdultFare()
                                );



                        case PREMIUM_ECONOMY ->

                                dto.setPremiumEconomyPrice(
                                        fare.getAdultFare()
                                );



                        case BUSINESS ->

                                dto.setBusinessPrice(
                                        fare.getAdultFare()
                                );



                        case FIRST ->

                                dto.setFirstPrice(
                                        fare.getAdultFare()
                                );

                    }



                    dto.setCurrency(
                            fare.getCurrency()
                    );


                });



        return dto;

    }







    private BaggagePolicyResDTO mapBaggage(
            BaggagePolicy baggage) {


        return BaggagePolicyResDTO
                .builder()


                .id(
                        baggage.getId()
                )


                .flightId(
                        baggage.getFlight()
                                .getId()
                )


                .flightNumber(
                        baggage.getFlight()
                                .getFlightNumber()
                )


                .cabinClass(
                        baggage.getCabinClass()
                )


                .cabinBaggageKg(
                        baggage.getCabinBaggageKg()
                )


                .checkinBaggageKg(
                        baggage.getCheckinBaggageKg()
                )


                .extraBaggagePricePerKg(
                        baggage.getExtraBaggagePricePerKg()
                )


                .build();

    }


}