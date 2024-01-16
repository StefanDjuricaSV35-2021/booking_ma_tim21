package com.example.booking_ma_tim21.retrofit;

import com.example.booking_ma_tim21.dto.AccommodationDetailsDTO;
import com.example.booking_ma_tim21.dto.AccommodationPreviewDTO;
import com.example.booking_ma_tim21.dto.AccommodationProfitDTO;
import com.example.booking_ma_tim21.dto.AccommodationReservationCountDTO;
import com.example.booking_ma_tim21.dto.ChartData;
import com.example.booking_ma_tim21.model.Accommodation;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface AnalyticsService {

    @GET("/api/v1/auth/accommodations/{ownerId}/profit")
    Call<List<ChartData>> getOwnerProfit(@Path("ownerId") Long ownerId, @Query("dateFrom")String dateFrom, @Query("dateTo") String dateTo);

    @GET("/api/v1/auth/accommodations/{ownerId}/res-count")
    Call<List<ChartData>> getOwnerReservationCount(@Path("ownerId") Long ownerId, @Query("dateFrom")String dateFrom, @Query("dateTo") String dateTo);


}
