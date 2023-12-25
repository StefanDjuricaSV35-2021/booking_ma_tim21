package com.example.booking_ma_tim21.retrofit;

import com.google.gson.Gson;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitService {

    public Retrofit getRetrofit() {
        return retrofit;
    }

    Retrofit retrofit;


    public RetrofitService() {
        initializeRetrofit();
    }

    private void initializeRetrofit() {

        retrofit=new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080")
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .build();
    }
}
