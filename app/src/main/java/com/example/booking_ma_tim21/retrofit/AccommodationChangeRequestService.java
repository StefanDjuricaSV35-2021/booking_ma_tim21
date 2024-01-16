package com.example.booking_ma_tim21.retrofit;

import com.example.booking_ma_tim21.dto.AccommodationChangeRequestDTO;
import com.example.booking_ma_tim21.model.Accommodation;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AccommodationChangeRequestService {
    @POST("/api/v1/auth/accommodationChangeRequests")
    Call<AccommodationChangeRequestDTO> createAccommodationChangeRequest(@Body AccommodationChangeRequestDTO accommodationChangeRequestDTO);
}
