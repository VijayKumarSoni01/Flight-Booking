package com.flightmanagement.flightmanagement.entity;

import jakarta.persistence.*;
import lombok.*;

import com.flightmanagement.flightmanagement.enums.CabinClass;

@Entity
@Table(
    name = "seats",
    uniqueConstraints = {
        @UniqueConstraint(
            columnNames = {"flight_id", "seat_number"}
        )
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "flight_id",
        nullable = false
    )
    private Flight flight;

    @Column(
        name = "seat_number",
        nullable = false,
        length = 10
    )
    private String seatNumber;

    @Column(
        name = "seat_index",
        nullable = false
    )
    private Integer seatIndex;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CabinClass cabinClass;
}