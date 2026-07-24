package com.flightmanagement.flightmanagement.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
@Table(
    name = "flight_amenities",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = "flight_id")
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FlightAmenity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "flight_id",
        nullable = false,
        unique = true
    )
    private Flight flight;

    @Builder.Default
    private Boolean mealIncluded = false;

    @Builder.Default
    private Boolean wifiAvailable = false;

    @Builder.Default
    private Boolean usbCharging = false;

    @Builder.Default
    private Boolean entertainmentSystem = false;
}