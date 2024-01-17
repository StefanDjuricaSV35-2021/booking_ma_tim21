package com.example.booking_ma_tim21.retrofit;


import com.example.booking_ma_tim21.dto.ReservationDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ReservationService {
    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type: application/json"
    })
    @POST("/api/v1/auth/reservations")
    Call<ReservationDTO> createReservation(@Body ReservationDTO req);

    @GET("/api/v1/auth/reservations/{id}")
    Call<ReservationDTO> getReservation(@Path("id") Long id);

    @GET("/api/v1/auth/reservations")
    Call<List<ReservationDTO>> getReservations();

    @PUT("/api/v1/auth/reservations")
    Call<ReservationDTO> updateReservation(@Body ReservationDTO req);

    @DELETE("/api/v1/auth/reservations/{id}")
    Call<Void> deleteReservation(@Path("id") Long id);

    @GET("/api/v1/auth/reservations/{userId}/reservations")
    Call<List<ReservationDTO>> getUserReservations(@Path("userId") Long userId);

    @GET("/api/v1/auth/reservations/{userId}/currentReservations")
    Call<List<ReservationDTO>> getCurrentReservations(@Path("userId") Long userId);

    @GET("/api/v1/auth/reservations/{ownerId}/currentOwnersReservations")
    Call<List<ReservationDTO>> getCurrentOwnersReservations(@Path("ownerId") Long ownerId);
}

