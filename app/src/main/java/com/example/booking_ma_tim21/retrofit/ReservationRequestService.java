package com.example.booking_ma_tim21.retrofit;

import com.example.booking_ma_tim21.dto.ReservationRequestDTO;
import com.example.booking_ma_tim21.model.Accommodation;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ReservationRequestService {
    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @POST("/api/v1/auth/reservationRequests")
    Call<ReservationRequestDTO> createReservationRequest(@Body ReservationRequestDTO req);
}
