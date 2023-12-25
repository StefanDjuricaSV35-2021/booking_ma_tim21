package com.example.booking_ma_tim21.retrofit;


import com.example.booking_ma_tim21.dto.UserDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserService {
    @GET("/api/v1/auth/users")
    Call<UserDTO> getUser(@Query("id") Long id);

    @GET("/api/v1/auth/users/email/{email}")
    Call<UserDTO> getUser(@Path("email") String email);

    @DELETE("/api/v1/auth/users/{id}")
    Call<Void> deleteUser(@Path("id") Long id);

    @POST("/api/v1/auth/users")
    Call<UserDTO> createUser(@Body UserDTO user);

    @PUT("/api/v1/auth/users")
    Call<UserDTO> updateUser(@Body UserDTO userDTO);

    @GET("/api/v1/auth/users/email/{email}")
    Call<UserDTO> getUser(@Path("email") String email);
}
