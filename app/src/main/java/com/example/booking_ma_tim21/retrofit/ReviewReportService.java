package com.example.booking_ma_tim21.retrofit;

import com.example.booking_ma_tim21.dto.ReviewReportDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ReviewReportService {
    @GET("/api/v1/auth/reports/reviews")
    Call<List<ReviewReportDTO>> findAll();

    @GET("/api/v1/auth/reports/reviews/{id}")
    Call<ReviewReportDTO> getReviewReport(@Path("id") Long id);

    @GET("/api/v1/auth/reports/reviews/owner")
    Call<List<ReviewReportDTO>> findAllOwnerReports(@Path("id") Long id);

    @GET("/api/v1/auth/reports/reviews/accommodation")
    Call<List<ReviewReportDTO>> getAccommodationReviewReports();

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @POST("/api/v1/auth/reports/reviews")
    Call<ReviewReportDTO> createReviewReport(@Body ReviewReportDTO reviewReportDTO);

    @DELETE("/api/v1/auth/reports/reviews/{id}")
    Call<Void> deleteReviewReport(@Path("id") Long id);
}
