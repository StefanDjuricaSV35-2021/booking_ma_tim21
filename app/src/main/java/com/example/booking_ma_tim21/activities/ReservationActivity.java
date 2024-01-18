package com.example.booking_ma_tim21.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.booking_ma_tim21.R;
import com.example.booking_ma_tim21.adapter.ReservationAdapter;
import com.example.booking_ma_tim21.authentication.AuthManager;
import com.example.booking_ma_tim21.dto.AccommodationDetailsDTO;
import com.example.booking_ma_tim21.dto.ReservationDTO;
import com.example.booking_ma_tim21.retrofit.AccommodationService;
import com.example.booking_ma_tim21.retrofit.ReservationService;
import com.example.booking_ma_tim21.retrofit.RetrofitService;
import com.example.booking_ma_tim21.util.NavigationSetup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReservationActivity extends AppCompatActivity {
    AccommodationService accService;
    AuthManager auth;
    ReservationService resService;
    private RecyclerView recyclerView;
    private ReservationAdapter reservationAdapter;
    List<ReservationDTO> reqs=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);

        auth=AuthManager.getInstance(this);

        NavigationSetup.setupNavigation(this, auth);

        RetrofitService ref=new RetrofitService();
        resService=ref.getRetrofit().create(ReservationService.class);
        accService=ref.getRetrofit().create(AccommodationService.class);

        recyclerView = findViewById(R.id.reservationRecyclerView);

        setReservations();
    }


    private void setReservations() {
        Call call=null;

        if(auth.getUserRole().equals("OWNER")){
            call=resService.getCurrentOwnersReservations(auth.getUserIdLong());
        }else{
            call=resService.getCurrentReservations(auth.getUserIdLong());

        }

        call.enqueue(new Callback<List<ReservationDTO>>() {
            @Override
            public void onResponse(Call<List<ReservationDTO>> call, Response<List<ReservationDTO>> response) {
                if (response.code() == 200) {

                    Log.d("REZ", "Meesage recieved");

                    reqs=response.body();
                    setNames();
                    setRecyclerView(response.body());

                } else {
                    Log.d("REZ", "Meesage recieved: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<ReservationDTO>> call, Throwable t) {
                t.printStackTrace();
            }


        });
    }

    void setRecyclerView(List<ReservationDTO> reqs){

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        reservationAdapter = new ReservationAdapter(this, reqs);
        recyclerView.setAdapter(reservationAdapter);

    }

    void setNames(){

        for(ReservationDTO req:reqs){

            Call call=accService.getAccommodation(req.getAccommodationId());

            call.enqueue(new Callback<AccommodationDetailsDTO>() {
                @Override
                public void onResponse(Call<AccommodationDetailsDTO> call, Response<AccommodationDetailsDTO> response) {
                    if (response.code() == 200){

                        Log.d("REZ","Meesage recieved");
                    }else{
                        Log.d("REZ","Meesage recieved: "+response.code());
                    }
                }

                @Override
                public void onFailure(Call<AccommodationDetailsDTO> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        NavigationSetup.closeDrawer(findViewById(R.id.drawerLayout));
    }
}