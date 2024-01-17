package com.example.booking_ma_tim21.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;

import com.example.booking_ma_tim21.R;
import com.example.booking_ma_tim21.authentication.AuthManager;
import com.example.booking_ma_tim21.dto.AccommodationPreviewDTO;
import com.example.booking_ma_tim21.fragments.AccommodatioPreviewRecycleViewFragment;
import com.example.booking_ma_tim21.retrofit.FavoriteAccommodationService;
import com.example.booking_ma_tim21.retrofit.RetrofitService;
import com.example.booking_ma_tim21.util.NavigationSetup;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoritesActivity extends AppCompatActivity {


    Long userId;
    FavoriteAccommodationService favoriteAccommodationService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AuthManager auth=AuthManager.getInstance(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        NavigationSetup.setupNavigation(this, auth);
        userId = auth.getUserIdLong();

        RetrofitService retrofitService= new RetrofitService();
        favoriteAccommodationService = retrofitService.getRetrofit().create(FavoriteAccommodationService .class);
        getFavorites();
    }


    void getFavorites(){
        Call call=favoriteAccommodationService.getUsersFavoritesPreviews(userId);

        call.enqueue(new Callback<List<AccommodationPreviewDTO>>() {
            @Override
            public void onResponse(Call<List<AccommodationPreviewDTO>> call, Response<List<AccommodationPreviewDTO>> response) {
                if (response.code() == 200) {

                    Log.d("REZ", "Meesage recieved");

                    setPreviewFragment(response.body());

                } else {
                    Log.d("REZ", "Meesage recieved: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<AccommodationPreviewDTO>> call, Throwable t) {
                t.printStackTrace();
            }


        });

    }

    void setPreviewFragment(List<AccommodationPreviewDTO> previews){
        AccommodatioPreviewRecycleViewFragment fragment = AccommodatioPreviewRecycleViewFragment.newInstance((ArrayList<AccommodationPreviewDTO>) previews);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.preview_recycler_fragment, fragment);
        transaction.commit();
    }


}