package com.project.notificationmanagement.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.project.notificationmanagement.config.feign.FeignConfig;
import com.project.notificationmanagement.dto.response.UserResponse;


@FeignClient(
        name = "user-management-service",
        url = "${services.user-management.url}",
        configuration = FeignConfig.class
)
public interface UserManagementServiceClient {


    @GetMapping(
            "/api/internal/users/{userId}"
    )
    UserResponse getUserById(
            @PathVariable("userId") Long userId);

}