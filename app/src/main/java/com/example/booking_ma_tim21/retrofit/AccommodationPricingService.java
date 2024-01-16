package com.example.booking_ma_tim21.retrofit;

import com.example.booking_ma_tim21.model.AccommodationPricing;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface AccommodationPricingService {

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @POST("/api/v1/auth/pricings")
    Call<AccommodationPricing> createAccommodationPricing(@Body AccommodationPricing accommodationPricing);
    @GET("/api/v1/auth/pricings/{accommodationId}/accommodationPricings")
    Call<List<AccommodationPricing>> getPricingsForAccommodation(@Path("accommodationId") Long accommodationId);



}

