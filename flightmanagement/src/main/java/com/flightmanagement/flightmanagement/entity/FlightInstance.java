package com.flightmanagement.flightmanagement.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.flightmanagement.flightmanagement.enums.FlightStatus;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "flight_instances")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlightInstance {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "schedule_id",
            nullable = false
    )
    private FlightSchedule schedule;


    @Column(nullable = false)
    private LocalDate flightDate;


    private LocalDateTime departureTime;


    private LocalDateTime arrivalTime;


    @Enumerated(EnumType.STRING)
    @Builder.Default
    private FlightStatus status = FlightStatus.SCHEDULED;

}