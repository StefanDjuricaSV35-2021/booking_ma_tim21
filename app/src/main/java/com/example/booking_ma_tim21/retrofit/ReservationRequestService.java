package com.example.booking_ma_tim21.retrofit;

import com.example.booking_ma_tim21.dto.AccommodationDetailsDTO;
import com.example.booking_ma_tim21.dto.ReservationDTO;
import com.example.booking_ma_tim21.dto.ReservationRequestDTO;
import com.example.booking_ma_tim21.model.Accommodation;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ReservationRequestService {
    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @POST("/api/v1/auth/reservationRequests")
    Call<ReservationRequestDTO> createReservationRequest(@Body ReservationRequestDTO req);


    @GET("/api/v1/auth/reservationRequests/{userId}/reservationRequests")
    Call<List<ReservationRequestDTO>> getUserReservationRequests(@Path("userId") Long userId);

    @PUT("/api/v1/auth/reservationRequests")
    Call<ReservationRequestDTO> updateReservationRequest(@Body ReservationRequestDTO req);

    @GET("/api/v1/auth/reservationRequests/{userId}/ownerReservationRequests")
    Call<List<ReservationRequestDTO>> getOwnerReservationRequests(@Path("userId") Long userId);
}
