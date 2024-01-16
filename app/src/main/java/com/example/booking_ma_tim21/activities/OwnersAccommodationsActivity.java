package com.example.booking_ma_tim21.activities;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.example.booking_ma_tim21.R;
import com.example.booking_ma_tim21.authentication.AuthManager;
import com.example.booking_ma_tim21.dto.AccommodationPreviewDTO;
import com.example.booking_ma_tim21.dto.UserDTO;
import com.example.booking_ma_tim21.fragments.AccommodatioPreviewRecycleViewFragment;
import com.example.booking_ma_tim21.retrofit.AccommodationService;
import com.example.booking_ma_tim21.retrofit.RetrofitService;
import com.example.booking_ma_tim21.retrofit.UserService;
import com.example.booking_ma_tim21.util.NavigationSetup;

import java.util.ArrayList;
import java.util.List;


public class OwnersAccommodationsActivity extends AppCompatActivity {
    AuthManager authManager;
    AccommodationService service;
    UserService userService;
    List<AccommodationPreviewDTO> previews;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owners_accommodations);
        authManager = AuthManager.getInstance(getApplicationContext());
        NavigationSetup.setupNavigation(this, authManager);

        RetrofitService retrofitService= new RetrofitService();
        userService=retrofitService.getRetrofit().create(UserService.class);
        service=retrofitService.getRetrofit().create(AccommodationService.class);

        initializeUserAndAccommodations();


    }

    private void initializeUserAndAccommodations() {
        authManager = this.getAuthManager();
        String email = authManager.getUserId();
        Call<UserDTO> call = userService.getUser(email);
        call.enqueue(new Callback<UserDTO>() {
            @Override
            public void onResponse(Call<UserDTO> call, Response<UserDTO> response) {
                if (response.isSuccessful()) {
                    UserDTO loggedInUser = response.body();
                    Call<List<AccommodationPreviewDTO>> newCall = service.getOwnersAccommodations(loggedInUser.getId());
                    newCall.enqueue(new Callback<List<AccommodationPreviewDTO>>() {
                        @Override
                        public void onResponse(Call<List<AccommodationPreviewDTO>> call, Response<List<AccommodationPreviewDTO>> response) {
                            if (response.isSuccessful()) {
                                previews = response.body();

                                AccommodatioPreviewRecycleViewFragment fragment = AccommodatioPreviewRecycleViewFragment.newInstance((ArrayList<AccommodationPreviewDTO>) previews);
                                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                                transaction.replace(R.id.preview_recycler_fragment, fragment);
                                transaction.commit();
                            } else {
                                Log.d("REZ","Meesage recieved: "+response.code());
                            }
                        }

                        @Override
                        public void onFailure(Call<List<AccommodationPreviewDTO>> call, Throwable t) {
                            t.printStackTrace();
                        }
                    });
                } else {
                    Log.d("REZ","Meesage recieved: "+response.code());
                }
            }

            @Override
            public void onFailure(Call<UserDTO> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        NavigationSetup.closeDrawer(findViewById(R.id.drawerLayout));
    }

    public AuthManager getAuthManager() {
        return authManager;
    }
}