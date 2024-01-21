package com.example.booking_ma_tim21.retrofit;

import com.example.booking_ma_tim21.dto.NotificationDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface NotificationsService {

    @GET("/api/v1/auth/notifications/{userId}")
    Call<List<NotificationDTO>> getUserNotifications(@Path("userId") Long userId);

    @DELETE("/api/v1/auth/notifications/{id}")
    Call<Void> deleteNotifications(@Path("id") Long id);
}
