package com.example.booking_ma_tim21.retrofit;

import com.example.booking_ma_tim21.dto.AccommodationChangeRequestDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface AccommodationChangeRequestService {
    @POST("/api/v1/auth/accommodationChangeRequests")
    Call<AccommodationChangeRequestDTO> createAccommodationChangeRequest(@Body AccommodationChangeRequestDTO accommodationChangeRequestDTO);

    @GET("/api/v1/auth/accommodationChangeRequests")
    Call<List<AccommodationChangeRequestDTO>> getPendingAccommodationChangeRequests();
    @GET("/api/v1/auth/accommodationChangeRequests/{id}")
    Call<AccommodationChangeRequestDTO> getAccommodationChangeRequest(@Path("id") Long id);
    @DELETE("/api/v1/auth/accommodationChangeRequests/{id}")
    Call<Void> deleteAccommodationChangeRequest(@Path("id") Long id);
}
