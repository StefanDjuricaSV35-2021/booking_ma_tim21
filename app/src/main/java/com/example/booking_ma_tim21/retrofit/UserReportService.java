package com.example.booking_ma_tim21.retrofit;

import com.example.booking_ma_tim21.dto.UserDTO;
import com.example.booking_ma_tim21.dto.UserReportDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserReportService {

    @GET("/api/v1/auth/reports/users")
    Call<List<UserReportDTO>> getUserReports();

    @GET("/api/v1/auth/reports/users/guest/{id}")
    Call<List<UserDTO>> getGuestsOwners(@Path("id") Long id);

    @GET("/api/v1/auth/reports/users/owner/{id}")
    Call<List<UserDTO>> getOwnersGuests(@Path("id") Long id);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @POST("/api/v1/auth/reports/users")
    Call<UserReportDTO> createUserReport(@Body UserReportDTO userReportDTO);

    @DELETE("/api/v1/auth/reports/users/{id}")
    Call<Void> deleteReport(@Path("id") Long id);
}
