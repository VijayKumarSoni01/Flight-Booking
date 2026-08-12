package com.flightmanagement.flightmanagement.entity;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import com.flightmanagement.flightmanagement.enums.CurrencyCode;
import com.flightmanagement.flightmanagement.enums.FlightStatus;
import com.flightmanagement.flightmanagement.enums.FlightType;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "flights", indexes = {

        @Index(name = "idx_flight_number", columnList = "flightNumber"),

        @Index(name = "idx_departure_time", columnList = "departureTime"),

        @Index(name = "idx_route", columnList = "origin_airport_id,destination_airport_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Flight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 10)
    private String flightNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "airline_id", nullable = false)
    private Airline airline;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "origin_airport_id", nullable = false)
    private Airport originAirport;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destination_airport_id", nullable = false)
    private Airport destinationAirport;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aircraft_id", nullable = false)
    private Aircraft aircraft;

    @OneToMany(mappedBy = "flight", fetch = FetchType.LAZY)
    private List<FlightFare> flightFares;

    @OneToMany(mappedBy = "flight", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BaggagePolicy> baggagePolicies;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FlightType flightType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private FlightStatus status = FlightStatus.SCHEDULED;

    /*
     * ADD THIS
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    @Builder.Default
    private CurrencyCode currency = CurrencyCode.INR;

    @Column(nullable = false)
    private LocalDateTime departureTime;

    @Column(nullable = false)
    private LocalDateTime arrivalTime;

    @Column(nullable = false)
    private Integer durationMinutes;

    @Column(length = 20)
    private String departureTerminal;

    @Column(length = 20)
    private String arrivalTerminal;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Version
    private Long version;

    @PrePersist
    public void onCreate() {

        validateTimes();

        LocalDateTime now = LocalDateTime.now();

        this.createdAt = now;

        this.updatedAt = now;

        this.durationMinutes = (int) Duration.between(
                departureTime,
                arrivalTime)
                .toMinutes();

        if (this.currency == null) {

            this.currency = CurrencyCode.INR;
        }

    }

    @PreUpdate
    public void onUpdate() {

        validateTimes();

        this.updatedAt = LocalDateTime.now();

        this.durationMinutes = (int) Duration.between(
                departureTime,
                arrivalTime)
                .toMinutes();

    }

    private void validateTimes() {

        if (arrivalTime.isBefore(departureTime)) {

            throw new IllegalArgumentException(
                    "Arrival time cannot be before departure time.");
        }

        if (arrivalTime.equals(departureTime)) {

            throw new IllegalArgumentException(
                    "Arrival time cannot be same as departure time.");

        }

        if (departureTime.isBefore(LocalDateTime.now())) {

            throw new IllegalArgumentException(
                    "Departure time must be in future.");

        }

    }

}