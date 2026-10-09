package com.flightmanagement.flightmanagement.entity;

import java.time.LocalDateTime;

import com.flightmanagement.flightmanagement.enums.SeatStatus;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
    name = "flight_instance_seats",
    uniqueConstraints = {
        @UniqueConstraint(
            columnNames = {
                "flight_instance_id",
                "seat_id"
            }
        )
    }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlightInstanceSeat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "flight_instance_id",
        nullable = false
    )
    private FlightInstance flightInstance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "seat_id",
        nullable = false
    )
    private Seat seat;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private SeatStatus seatStatus = SeatStatus.AVAILABLE;

    @Column(length = 30)
    private String bookingReference;

    private LocalDateTime reservedAt;

    @Version
    private Long version;
}