package com.example.booking_ma_tim21.retrofit;

import com.example.booking_ma_tim21.dto.AccommodationPreviewDTO;
import com.example.booking_ma_tim21.dto.FavoriteAccommodationDTO;
import com.example.booking_ma_tim21.model.Accommodation;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface FavoriteAccommodationService {



    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @POST("/api/v1/auth/favorite/accommodations")
    Call<Accommodation> addFavoriteAccommodation(@Body FavoriteAccommodationDTO accommodation);

    @GET("/api/v1/auth/favorite/accommodations/is-favorite")
    Call<Boolean> isUsersFavorite(@Query("accId")Long accId,@Query("userId")Long userId);

    @GET("/api/v1/auth/favorite/accommodations/user/{id}/previews")
    Call<List<AccommodationPreviewDTO>> getUsersFavoritesPreviews(@Path("id") Long userId);

    @DELETE("/api/v1/auth/favorite/accommodations")
    Call<Void> deleteFavoritesAccommodation(@Query("accId")Long accId,@Query("userId")Long userId);
}
