package com.project.usermanagment.mapper;

import com.project.usermanagment.dtos.PassengerDTO.PassengerResponseDTO;
import com.project.usermanagment.entity.Passenger;

public class PassengerMapper {

    public static PassengerResponseDTO toResponse(Passenger p) {

        return PassengerResponseDTO.builder()
                .id(p.getId())
                .title(p.getTitle())
                .firstName(p.getFirstName())
                .middleName(p.getMiddleName())
                .lastName(p.getLastName())
                .dateOfBirth(p.getDateOfBirth())
                .gender(p.getGender())
                .nationality(p.getNationality())
                .passengerType(p.getPassengerType())
                .passportNumber(p.getPassportNumber())
                .passportExpiry(p.getPassportExpiry())
                .issuingCountry(p.getIssuingCountry())
                .build();
    }
}
