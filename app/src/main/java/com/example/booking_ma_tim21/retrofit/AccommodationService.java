package com.example.booking_ma_tim21.retrofit;

import com.example.booking_ma_tim21.dto.AccommodationDetailsDTO;
import com.example.booking_ma_tim21.dto.AccommodationPreviewDTO;
import com.example.booking_ma_tim21.model.Accommodation;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface AccommodationService {

    @GET("/api/v1/auth/accommodations/previews")
    Call<List<AccommodationPreviewDTO>> getAllAccommodations();

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @POST("/api/v1/auth/accommodations")
    Call<Accommodation> createAccommodation(@Body Accommodation accommodation);

    @GET("/api/v1/auth/accommodations/{id}")
    Call<AccommodationDetailsDTO> getAccommodation(@Path("id") Long id);

    @GET("/api/v1/auth/accommodations/search")
    Call<List<AccommodationPreviewDTO>> getFilteredAccommodations(@Query("location") String location,@Query("noGuests")String noGuests,@Query("dateFrom")String dateFrom,@Query("dateTo")String dateTo,@Query("filters")String filters);
}
