package com.example.booking_ma_tim21.retrofit;

import com.example.booking_ma_tim21.dto.AccommodationPreviewDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface AccommodationService {

    @GET("/accommodations")
    Call<List<AccommodationPreviewDTO>> getAllAccommodations();
}
