package com.example.booking_ma_tim21.retrofit;


import com.example.booking_ma_tim21.dto.AccommodationReviewDTO;
import com.example.booking_ma_tim21.dto.ReviewReportDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface AccommodationReviewService {
    @GET("/api/v1/auth/reviews/accommodations")
    Call<List<AccommodationReviewDTO>> findAll();

    @GET("/api/v1/auth/reviews/accommodations/{id}")
    Call<List<AccommodationReviewDTO>> getAccommodationReviews(@Path("id") Long id);

    @GET("/api/v1/auth/reviews/accommodations/one/{id}")
    Call<AccommodationReviewDTO> getAccommodationReview(@Path("id") Long id);


    @GET("/api/v1/auth/reports/reviews/accommodations/{accId}/avg")
    Call<List<ReviewReportDTO>> getAverageAccommodationReview(@Path("accId") Long id);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @POST("/api/v1/auth/reviews/accommodations")
    Call<AccommodationReviewDTO> createAccommodationReview(@Body AccommodationReviewDTO accommodationReviewDTO);

    @DELETE("/api/v1/reviews/accommodations/{id}")
    Call<Void> deleteAccommodationReview(@Path("id") Long id);

}
