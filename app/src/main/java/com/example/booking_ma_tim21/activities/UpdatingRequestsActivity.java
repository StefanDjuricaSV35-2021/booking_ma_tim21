package com.example.booking_ma_tim21.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.booking_ma_tim21.R;
import com.example.booking_ma_tim21.authentication.AuthManager;
import com.example.booking_ma_tim21.dto.AccommodationChangeRequestDTO;
import com.example.booking_ma_tim21.dto.AccommodationPreviewDTO;
import com.example.booking_ma_tim21.fragments.AccommodatioPreviewRecycleViewFragment;
import com.example.booking_ma_tim21.fragments.AccommodationUpdatingPreviewRecycleViewFragment;
import com.example.booking_ma_tim21.retrofit.AccommodationChangeRequestService;
import com.example.booking_ma_tim21.retrofit.AccommodationService;
import com.example.booking_ma_tim21.retrofit.RetrofitService;
import com.example.booking_ma_tim21.util.NavigationSetup;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdatingRequestsActivity extends AppCompatActivity {
    AuthManager authManager;
    AccommodationChangeRequestService service;
    List<AccommodationChangeRequestDTO> changeRequestDTOS;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updating_requests);

        authManager = AuthManager.getInstance(getApplicationContext());
        NavigationSetup.setupNavigation(this, authManager);

        RetrofitService retrofitService= new RetrofitService();
        service=retrofitService.getRetrofit().create(AccommodationChangeRequestService.class);

        initializeAccommodations();
    }

    private void initializeAccommodations() {
        Call<List<AccommodationChangeRequestDTO>> newCall = service.getPendingAccommodationChangeRequests();
        newCall.enqueue(new Callback<List<AccommodationChangeRequestDTO>>() {
            @Override
            public void onResponse(Call<List<AccommodationChangeRequestDTO>> call, Response<List<AccommodationChangeRequestDTO>> response) {
                if (response.isSuccessful()) {
                    changeRequestDTOS = response.body();

                    AccommodationUpdatingPreviewRecycleViewFragment fragment = AccommodationUpdatingPreviewRecycleViewFragment.newInstance((ArrayList<AccommodationChangeRequestDTO>) changeRequestDTOS);
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.updating_preview_recycler_fragment, fragment);
                    transaction.commit();
                } else {
                    Log.d("REZ","Meesage recieved: "+response.code());
                }
            }

            @Override
            public void onFailure(Call<List<AccommodationChangeRequestDTO>> call, Throwable t) {
                t.printStackTrace();

            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        NavigationSetup.closeDrawer(findViewById(R.id.drawerLayout));
    }

    @Override
    protected void onResume() {
        super.onResume();
        initializeAccommodations();
    }
}