package com.example.booking_ma_tim21.retrofit;

import com.example.booking_ma_tim21.dto.AccommodationPricingChangeRequestDTO;
import com.example.booking_ma_tim21.model.AccommodationPricing;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AccommodationPricingChangeRequestService {
    @POST("/api/v1/auth/accommodationPricingChangeRequests")
    Call<AccommodationPricingChangeRequestDTO> createAccommodationPricingChangeRequest(@Body AccommodationPricingChangeRequestDTO accommodationPricingChangeRequestDTO);
}
