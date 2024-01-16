package com.example.booking_ma_tim21.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.LayoutTransition;
import android.content.Intent;
import android.opengl.Visibility;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.Fade;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.booking_ma_tim21.R;

import com.example.booking_ma_tim21.authentication.AuthManager;
import com.example.booking_ma_tim21.dto.AccommodationPreviewDTO;
import com.example.booking_ma_tim21.fragments.AccommodatioPreviewRecycleViewFragment;
import com.example.booking_ma_tim21.retrofit.AccommodationService;
import com.example.booking_ma_tim21.retrofit.RetrofitService;
import com.example.booking_ma_tim21.retrofit.UserService;
import com.example.booking_ma_tim21.util.NavigationSetup;

import com.example.booking_ma_tim21.adapter.PreviewAdapter;
import com.example.booking_ma_tim21.model.AccommodationPreview;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {

    AuthManager authManager;
    AccommodationService service;
    List<AccommodationPreviewDTO> previews;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        authManager = AuthManager.getInstance(getApplicationContext());
        NavigationSetup.setupNavigation(this, authManager);

        RetrofitService retrofitService= new RetrofitService();
        service=retrofitService.getRetrofit().create(AccommodationService.class);

        initializeAccommodations();
    }

    private void initializeAccommodations() {
        Call<List<AccommodationPreviewDTO>> newCall = service.getAllAccommodations();
        newCall.enqueue(new Callback<List<AccommodationPreviewDTO>>() {
            @Override
            public void onResponse(Call<List<AccommodationPreviewDTO>> call, Response<List<AccommodationPreviewDTO>> response) {
                if (response.isSuccessful()) {
                    previews = response.body();

                    AccommodatioPreviewRecycleViewFragment fragment = AccommodatioPreviewRecycleViewFragment.newInstance((ArrayList<AccommodationPreviewDTO>) previews);
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.preview_recycler_fragment_main, fragment);
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
    }

    @Override
    protected void onPause() {
        super.onPause();
        NavigationSetup.closeDrawer(findViewById(R.id.drawerLayout));
    }



    private void showToast(String message){
        Toast.makeText(this, message,Toast.LENGTH_SHORT).show();

    }
}