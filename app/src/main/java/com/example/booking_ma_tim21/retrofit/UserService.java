package com.example.booking_ma_tim21.retrofit;


import com.example.booking_ma_tim21.dto.UserDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface UserService {
    @GET("/users/7")
    Call<UserDTO> getUser();
}
