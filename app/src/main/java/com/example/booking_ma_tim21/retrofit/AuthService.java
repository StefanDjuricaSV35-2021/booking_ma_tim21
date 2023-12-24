package com.example.booking_ma_tim21.retrofit;

import com.example.booking_ma_tim21.model.JWTAuthenticationResponse;
import com.example.booking_ma_tim21.model.SignInRequest;
import com.example.booking_ma_tim21.model.SignUpRequest;
import com.example.booking_ma_tim21.model.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface AuthService {

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @POST("/api/v1/auth/signin")
    Call<JWTAuthenticationResponse> signIn(@Body SignInRequest signInRequest);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @POST("/api/v1/auth/signin")
    Call<User> signup(@Body SignUpRequest signUpRequest);

}
