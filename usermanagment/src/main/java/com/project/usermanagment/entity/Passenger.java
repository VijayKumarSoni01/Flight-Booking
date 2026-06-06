package com.project.usermanagment.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.project.usermanagment.enumFolder.Gender;
import com.project.usermanagment.enumFolder.PassengerType;
import com.project.usermanagment.enumFolder.Title;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "passengers", indexes = {
        @Index(name = "idx_passenger_user", columnList = "user_id")
}, uniqueConstraints = {
        @UniqueConstraint(columnNames = { "passport_number", "user_id" })
})
public class Passenger {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Title title;

    @NotBlank
    @Column(length = 50, nullable = false)
    private String firstName;

    @Column(length = 50)
    private String middleName;

    @NotBlank
    @Column(length = 50, nullable = false)
    private String lastName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull
    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @NotBlank
    @Column(length = 100, nullable = false)
    private String nationality;

    @Enumerated(EnumType.STRING)
    private PassengerType passengerType;

    @Column(length = 20)
    private String passportNumber;

    @Future(message = "Passport expiry must be in future")
    private LocalDate passportExpiry;

    private String issuingCountry;

    @Builder.Default
    @Column(nullable = false)
    private Boolean isActive = true;

    // @Version
    // private Long version;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    void onCreate() {
        createdAt = updatedAt = LocalDateTime.now();
        calculatePassengerType();
    }

    @PreUpdate
    void onUpdate() {
        updatedAt = LocalDateTime.now();
        calculatePassengerType();
    }

    private void calculatePassengerType() {
        if (dateOfBirth == null)
            return;

        int age = java.time.Period.between(dateOfBirth, LocalDate.now()).getYears();

        if (age <= 2) {
            passengerType = PassengerType.INFANT;
        } else if (age < 12) {
            passengerType = PassengerType.CHILD;
        } else {
            passengerType = PassengerType.ADULT;
        }
    }
}