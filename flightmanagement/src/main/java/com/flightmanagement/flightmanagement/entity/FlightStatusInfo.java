package com.flightmanagement.flightmanagement.entity;

import java.time.LocalDateTime;

import com.flightmanagement.flightmanagement.enums.FlightStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "flight_status", uniqueConstraints = {
        @UniqueConstraint(columnNames = "flight_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FlightStatusInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "flight_id", nullable = false, unique = true)
    private Flight flight;

    private LocalDateTime estimatedDeparture;

    private LocalDateTime estimatedArrival;

    private LocalDateTime actualDeparture;

    private LocalDateTime actualArrival;

    @Column(nullable = false)
    @Builder.Default
    private Integer delayMinutes = 0;

    @Column(length = 10)
    private String departureGate;

    @Column(length = 10)
    private String arrivalGate;

    private LocalDateTime lastApiSync;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private FlightStatus status = FlightStatus.SCHEDULED;
}