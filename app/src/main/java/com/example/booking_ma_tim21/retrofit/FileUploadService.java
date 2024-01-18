package com.example.booking_ma_tim21.retrofit;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface FileUploadService {
    @Multipart
    @POST("/api/v1/auth/images/upload/mobile")
    Call<List<String>> uploadFiles(@Part List<MultipartBody.Part> files);
}
