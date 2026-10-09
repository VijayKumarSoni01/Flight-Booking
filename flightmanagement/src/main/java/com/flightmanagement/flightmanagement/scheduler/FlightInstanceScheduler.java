package com.flightmanagement.flightmanagement.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.flightmanagement.flightmanagement.service.interFace.FlightInstanceService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class FlightInstanceScheduler {

    private final FlightInstanceService flightInstanceService;

    @Scheduled(
            initialDelay = 10000,
            fixedRate = 86400000
    )
    public void generateInstances() {

        flightInstanceService.generateFutureInstances();
    }
}
