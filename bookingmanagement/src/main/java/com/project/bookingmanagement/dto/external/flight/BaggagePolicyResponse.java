package com.project.bookingmanagement.dto.external.flight;

import lombok.Data;

@Data
public class BaggagePolicyResponse {

    private Integer cabinBaggageWeight;

    private Integer checkedBaggageWeight;

    private String baggageWeightUnit;

    private String baggagePolicyDescription;
}