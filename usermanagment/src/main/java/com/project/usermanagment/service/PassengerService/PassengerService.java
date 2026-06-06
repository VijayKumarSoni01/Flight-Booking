package com.project.usermanagment.service.PassengerService;

import java.util.List;

import org.springframework.stereotype.Service;

import com.project.usermanagment.dtos.PassengerDTO.PassengerResponseDTO;
import com.project.usermanagment.dtos.PassengerDTO.PassengerRequestDTO;
import com.project.usermanagment.dtos.PassengerDTO.UpdatePassengerDTO;
import com.project.usermanagment.entity.Passenger;
import com.project.usermanagment.entity.User;
import com.project.usermanagment.exception.UnauthorizedAccessException;
import com.project.usermanagment.mapper.PassengerMapper;
import com.project.usermanagment.repository.PassengerRepository;
import com.project.usermanagment.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PassengerService {

    private final PassengerRepository passengerRepository;
    private final UserRepository userRepository;

    public PassengerResponseDTO addPassanger(String email, PassengerRequestDTO request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not Found"));

        Passenger passenger = Passenger.builder()
                .title(request.getTitle())
                .firstName(request.getFirstName())
                .middleName(request.getMiddleName())
                .lastName(request.getLastName())
                .dateOfBirth(request.getDateOfBirth())
                .gender(request.getGender())
                .nationality(request.getNationality())
                .passportNumber(request.getPassportNumber())
                .passportExpiry(request.getPassportExpiry())
                .issuingCountry(request.getIssuingCountry())
                .user(user)
                .build();

        return PassengerMapper.toResponse(passengerRepository.save(passenger));
    }

    public List<PassengerResponseDTO> getMyPassengers(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Passenger> passengers = passengerRepository.findByUserId(user.getId());

        return passengers.stream()
                .map(p -> PassengerResponseDTO.builder()
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
                        .build())
                .toList();

    }

    public PassengerResponseDTO updatePassenger(Long id, String email, UpdatePassengerDTO req) {
        Passenger p = getOwnedPassenger(id, email);

        boolean hashFirst = req.getFirstName() != null;
        boolean hashLast = req.getLastName() != null;

        if (hashFirst || hashLast) {
            if (!(hashFirst && hashLast)) {
                throw new IllegalArgumentException("Both first name and last name must be provided together");
            }
            p.setFirstName(req.getFirstName());
            p.setLastName(req.getLastName());
            p.setMiddleName(req.getMiddleName());
        }

        if (req.getTitle() != null)
            p.setTitle(req.getTitle());

        if (req.getDateOfBirth() != null)
            p.setDateOfBirth(req.getDateOfBirth());

        if (req.getGender() != null)
            p.setGender(req.getGender());

        if (req.getNationality() != null)
            p.setNationality(req.getNationality());

        if (req.getPassportNumber() != null)
            p.setPassportNumber(req.getPassportNumber());

        if (req.getPassportExpiry() != null)
            p.setPassportExpiry(req.getPassportExpiry());

        if (req.getIssuingCountry() != null)
            p.setIssuingCountry(req.getIssuingCountry());

        Passenger saved = passengerRepository.save(p);

        return PassengerResponseDTO.builder()
                .id(saved.getId())
                .title(saved.getTitle())
                .firstName(saved.getFirstName())
                .middleName(saved.getMiddleName())
                .lastName(saved.getLastName())
                .dateOfBirth(saved.getDateOfBirth())
                .gender(saved.getGender())
                .nationality(saved.getNationality())
                .passengerType(saved.getPassengerType())
                .passportNumber(saved.getPassportNumber())
                .passportExpiry(saved.getPassportExpiry())
                .issuingCountry(saved.getIssuingCountry())
                .build();

    }

    private Passenger getOwnedPassenger(Long id, String email) {

        Passenger passenger = passengerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Passenger not found"));

        if (!passenger.getUser().getEmail().equals(email)) {
            throw new UnauthorizedAccessException("You cannot access this passenger");
        }

        return passenger;
    }

    public void deletePassenger(Long id, String email) {
        Passenger passenger = getOwnedPassenger(id, email);

        passengerRepository.delete(passenger);
    }

}
