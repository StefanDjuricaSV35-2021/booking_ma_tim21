package com.example.booking_ma_tim21.retrofit;

import com.example.booking_ma_tim21.dto.OwnerReviewDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface OwnerReviewService {

    @GET("/api/v1/auth/reviews/owners")
    Call<List<OwnerReviewDTO>> getAllOwnerReviews();

    @GET("/api/v1/auth/reviews/owners/{id}")
    Call<List<OwnerReviewDTO>> getOwnerReviews(@Path("id") Long id);

    @GET("/api/v1/auth/reviews/owners/one/{id}")
    Call<OwnerReviewDTO> getOwnerReview(@Path("id") Long id);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @POST("/api/v1/auth/reviews/owners")
    Call<OwnerReviewDTO> createOwnerReview(@Body OwnerReviewDTO ownerReviewDTO);

    @DELETE("/api/v1/auth/reviews/owners/{id}")
    Call<Void> deleteReviewReport(@Path("id") Long id);
}
