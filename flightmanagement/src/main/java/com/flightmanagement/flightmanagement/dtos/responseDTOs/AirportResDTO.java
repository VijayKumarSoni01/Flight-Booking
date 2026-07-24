package com.flightmanagement.flightmanagement.dtos.responseDTOs;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AirportResDTO {
    private Long id;

    private String iataCode;

    private String icaoCode;

    private String name;

    private String city;

    private String country;

    private Double latitude;

    private Double longitude;

    private String timezone;
}
