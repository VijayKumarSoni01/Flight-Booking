package com.project.usermanagment.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.project.usermanagment.enumFolder.Role;
import com.project.usermanagment.enumFolder.Title;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.usermanagment.enumFolder.Gender;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users", indexes = {
        @Index(name = "idx_user_email", columnList = "email"),
        @Index(name = "idx_user_phone", columnList = "phoneNumber"),
        @Index(name = "idx_user_username", columnList = "username"),
        @Index(name = "idx_user_active", columnList = "isActive")
})
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Title title;

    @NotBlank
    @Column(nullable = false, length = 50)
    private String firstName;

    private String middleName;

    @NotBlank
    @Column(nullable = false, length = 50)
    private String lastName;

    @NotBlank
    @Email
    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false, length = 255)
    @JsonIgnore
    private String password;

    @NotBlank
    @Size(min = 6, max = 30)
    @Pattern(regexp = "^[a-zA-Z0-9](?:[a-zA-Z0-9._]{4,28}[a-zA-Z0-9])$", message = "Invalid username")
    @Column(nullable = false, unique = true, length = 30)
    private String username;

    @Column(unique = true, length = 16)
    @Pattern(regexp = "^(\\+91)?[6-9][0-9]{9}$", message = "Phone must be valid Indian number")
    private String phoneNumber;

    @Column(length = 16)
    @Pattern(regexp = "^(\\+91)?[6-9][0-9]{9}$", message = "Alternate phone must be valid Indian number")
    private String alternatePhone;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    @NotNull(message = "Gender is required")
    private Gender gender;

    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

    @NotBlank
    private String nationality;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "role", length = 20)
    @Builder.Default
    private Set<Role> roles = new HashSet<>(Set.of(Role.USER));

    @Column(length = 100)
    private String addressLine1;

    @Column(length = 100)
    private String addressLine2;

    @Column(length = 50)
    private String city;

    @Column(length = 50)
    private String state;

    @Column(length = 100)
    private String country;

    @Column(length = 20)
    private String pinCode;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private Set<Passenger> passengers = new HashSet<>();

    @Builder.Default
    private boolean isActive = true;

    private LocalDateTime deletedAt;

    private Long deletedBy;

    @Builder.Default
    private boolean phoneVerified = false;

    @JsonIgnore
    @Column(length = 6)
    private String phoneOtp;

    @JsonIgnore
    private LocalDateTime otpExpiry;

    @Builder.Default
    private boolean emailVerified = false;

    @JsonIgnore
    @Column(length = 255)
    private String resetToken;

    @JsonIgnore
    private LocalDateTime resetTokenExpiry;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    private LocalDateTime lastLogin;

    @Builder.Default
    private int loginCount = 0;

    @Column(length = 45)
    private String lastLoginIp;

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public String getFullName() {
        if (middleName != null && !middleName.isBlank()) {
            return firstName + " " + middleName + " " + lastName;
        }
        return firstName + " " + lastName;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                .toList();
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isActive;
    }
}
