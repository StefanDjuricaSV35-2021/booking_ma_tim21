package com.example.booking_ma_tim21.retrofit;

import com.example.booking_ma_tim21.dto.AccommodationPreviewDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface AccommodationService {

    @GET("/api/v1/auth/accommodations/previews")
    Call<List<AccommodationPreviewDTO>> getAllAccommodations();

    @GET("/api/v1/auth/accommodations/search")
    Call<List<AccommodationPreviewDTO>> getSearchedAccommodations(@Query("location") String location,@Query("noGuests")String noGuests,@Query("dateFrom")String dateFrom,@Query("dateTo")String dateTo);

    @GET("/api/v1/auth/accommodations/search")
    Call<List<AccommodationPreviewDTO>> getFilteredAccommodations(@Query("location") String location,@Query("noGuests")String noGuests,@Query("dateFrom")String dateFrom,@Query("dateTo")String dateTo,@Query("filters")String filters);
}
