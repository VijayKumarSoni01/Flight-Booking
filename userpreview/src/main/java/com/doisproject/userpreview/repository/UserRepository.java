package com.doisproject.userpreview.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.doisproject.userpreview.model.User;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query("{ 'isActive': false, 'deactivatedAt': { $lte: ?0 } }")
    List<User> findUsersDeactivatedBefore(LocalDateTime cutoffDate);

}
