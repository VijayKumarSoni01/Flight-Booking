package com.flightmanagement.flightmanagement.entity;

import java.time.Duration;
import java.time.LocalDateTime;

import com.flightmanagement.flightmanagement.enums.FlightStatus;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "flights", indexes = {
        @Index(name = "idx_route", columnList = "source,destination"),
        @Index(name = "idx_departure", columnList = "departureTime")
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

    @NotBlank
    @Column(nullable = false, unique = true)
    private String flightNumber;

    private String carrierCode;
    private String airlineName;

    @NotBlank
    @Column(length = 3, nullable = false)
    private String source;

    @NotBlank
    @Column(length = 3, nullable = false)
    private String destination;

    @NotNull
    private LocalDateTime departureTime;

    @NotNull
    private LocalDateTime arrivalTime;

    private Long durationMinutes;

    @Positive
    private double price;

    @Min(1)
    private int totalSeats;

    @Min(0)
    private int availableSeats;

    private String departureTerminal;
    private String gate;

    private String aircraftType;

    @Enumerated(EnumType.STRING)
    private FlightStatus status;

    @Version
    private int version;

    @Builder.Default
    private boolean isActive = true;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    @PreUpdate
    public void normalize() {
        if (source != null)
            source = source.toUpperCase();
        if (destination != null)
            destination = destination.toUpperCase();
    }

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();

        if (this.availableSeats == 0) {
            this.availableSeats = this.totalSeats;
        }

        if (this.durationMinutes == null && departureTime != null && arrivalTime != null) {
            this.durationMinutes = Duration.between(departureTime, arrivalTime).toMinutes();
        }

        if (this.status == null) {
            this.status = FlightStatus.SCHEDULED;
        }
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}