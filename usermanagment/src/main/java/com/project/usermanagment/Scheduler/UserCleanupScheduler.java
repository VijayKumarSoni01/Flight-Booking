package com.project.usermanagment.Scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

import com.project.usermanagment.service.UserService.PublicUserService;

@Component
@RequiredArgsConstructor
public class UserCleanupScheduler {

    private final PublicUserService userService;

    @Scheduled(cron = "0 0 2 * * ?")
    public void cleanup() {
        userService.deleteExpiredUsers();
    }
}