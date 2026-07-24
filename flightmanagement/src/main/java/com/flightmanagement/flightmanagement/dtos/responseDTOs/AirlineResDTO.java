package com.flightmanagement.flightmanagement.dtos.responseDTOs;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AirlineResDTO {
    private Long id;

    private String iataCode;

    private String name;

    private String logoUrl;

    private Boolean active;
}
