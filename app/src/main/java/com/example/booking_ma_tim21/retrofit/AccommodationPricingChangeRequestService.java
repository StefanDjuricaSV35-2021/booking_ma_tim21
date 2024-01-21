package com.example.booking_ma_tim21.retrofit;

import com.example.booking_ma_tim21.dto.AccommodationChangeRequestDTO;
import com.example.booking_ma_tim21.dto.AccommodationPricingChangeRequestDTO;
import com.example.booking_ma_tim21.model.AccommodationPricing;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface AccommodationPricingChangeRequestService {
    @POST("/api/v1/auth/accommodationPricingChangeRequests")
    Call<AccommodationPricingChangeRequestDTO> createAccommodationPricingChangeRequest(@Body AccommodationPricingChangeRequestDTO accommodationPricingChangeRequestDTO);

    @GET("/api/v1/auth/accommodationPricingChangeRequests/all/{accommodationChangeRequestId}")
    Call<List<AccommodationPricingChangeRequestDTO>> getAllAccommodationPricingChangeRequests(@Path("accommodationChangeRequestId") Long accommodationChangeRequestId);

    @PUT("/api/v1/auth/accommodationPricingChangeRequests/update/{accommodationId}")
    Call<Void> updateAccommodationPricings(@Path("accommodationId") Long accommodationId, @Body List<AccommodationPricingChangeRequestDTO> accommodationPricingChangeRequestDTOS);

}
