package com.example.booking_ma_tim21.retrofit;

import com.example.booking_ma_tim21.dto.AccommodationDetailsDTO;
import com.example.booking_ma_tim21.dto.AccommodationPreviewDTO;
import com.example.booking_ma_tim21.model.Accommodation;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface FavoriteAccommodationService {



//    @Headers({
//            "User-Agent: Mobile-Android",
//            "Content-Type:application/json"
//    })
//    @POST("/api/v1/auth/accommodations")
//    Call<Accommodation> createAccommodation(@Body Accommodation accommodation);

//    @Headers({
//            "User-Agent: Mobile-Android",
//            "Content-Type: application/json"
//    })
//    @PUT("/api/v1/auth/accommodations")
//    Call<Accommodation> updateAccommodation(@Body Accommodation accommodation);


//    @GET("/api/v1/auth/accommodations/{id}")
//    Call<AccommodationDetailsDTO> getAccommodation(@Path("id") Long id);
//
//    @GET("/api/v1/auth/accommodations/search")
//    Call<List<AccommodationPreviewDTO>> getFilteredAccommodations(@Query("dateFrom")String dateFrom,@Query("dateTo") String dateTo,@Query("noGuests")Integer noGuests,@Query("location") String location,@Query("filters")String filters);

    @GET("/api/v1/auth/favorite/accommodations/is-favorite")
    Call<Boolean> isUsersFavorite(@Query("accId")Long accId,@Query("userId")Long userId);
//
//    @DELETE("/api/v1/accommodations/{id}")
//    Call<Void> deleteAccommodation(@Path("id") Long id);
}
