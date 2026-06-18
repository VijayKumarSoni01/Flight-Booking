package com.project.usermanagment.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.project.usermanagment.entity.User;
import com.project.usermanagment.enumFolder.Role;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.email = :login OR u.phoneNumber = :login OR u.username = :login")
    Optional<User> findByLogin(@Param("login") String login);

    Optional<User> findByEmail(String email);

    Optional<User> findByEmailAndIsActiveTrue(String email);

    List<User> findByIsActiveTrue();

    List<User> findByIsActiveFalse();

    @Query("SELECT u FROM User u WHERE u.isActive = false AND u.deletedAt <= :cutoff")
    List<User> findUsersDeletedBefore(@Param("cutoff") LocalDateTime cutoff);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByPhoneNumber(String normalized);

    Optional<User> findByResetToken(String token);

    @Query("""
                SELECT DISTINCT u FROM User u
                JOIN u.roles r
                WHERE u.isActive = true AND
                (:search IS NULL OR
                    LOWER(u.email) LIKE LOWER(CONCAT('%', :search, '%')) OR
                    LOWER(u.username) LIKE LOWER(CONCAT('%', :search, '%')))
                AND (:role IS NULL OR r = :role)
            """)
    Page<User> searchUsers(
            @Param("search") String search,
            @Param("role") Role role,
            Pageable pageable);

    @Query("SELECT COUNT(u) FROM User u JOIN u.roles r WHERE r = com.project.usermanagment.enumFolder.Role.ADMIN")
    long countAdmins();

    Optional<User> findByEmailVerificationToken(String token);

    Optional<User> findByPhoneNumber(String phoneNumber);
}
