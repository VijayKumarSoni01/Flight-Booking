package com.flightmanagement.flightmanagement.service.interFace;

import java.util.List;

import com.flightmanagement.flightmanagement.dtos.requestDTOs.BaggagePolicyReqDTO;
import com.flightmanagement.flightmanagement.dtos.responseDTOs.BaggagePolicyResDTO;
import com.flightmanagement.flightmanagement.enums.CabinClass;

public interface BaggagePolicyService {

    BaggagePolicyResDTO createBaggagePolicy(BaggagePolicyReqDTO request);

    BaggagePolicyResDTO getPolicyByFlightAndCabin(
            Long flightId,
            CabinClass cabinClass);

    List<BaggagePolicyResDTO> getAllBaggagePolicies();

    BaggagePolicyResDTO updateBaggagePolicy(Long id, BaggagePolicyReqDTO request);

    void deleteBaggagePolicy(Long id);
}
