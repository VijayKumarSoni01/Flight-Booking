package com.doisproject.userpreview.ServiceLayer;

import com.doisproject.userpreview.model.User;
import com.doisproject.userpreview.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AutoDeleteScheduler {

    private static final Logger log = LoggerFactory.getLogger(AutoDeleteScheduler.class);
    private final UserRepository userRepository;

    // Runs every day at midnight
    @Scheduled(cron = "0 0 0 * * *")
    public void deleteInactiveUsers() {

        // Find all users deactivated more than 30 days ago
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(30);
        List<User> usersToDelete = userRepository.findUsersDeactivatedBefore(cutoffDate);

        if (usersToDelete.isEmpty()) {
            log.info("Auto-delete: No inactive users to delete today.");
            return;
        }

        for (User user : usersToDelete) {
            log.info("Auto-deleting user: {} | Deactivated at: {}",
                    user.getEmail(), user.getDeactivatedAt());
            userRepository.delete(user);
        }

        log.info("Auto-delete complete: {} user(s) deleted.", usersToDelete.size());
    }
}