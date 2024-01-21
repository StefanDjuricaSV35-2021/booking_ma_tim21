package com.example.booking_ma_tim21.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.booking_ma_tim21.R;
import com.example.booking_ma_tim21.adapter.ReservationRequestsAdapter;
import com.example.booking_ma_tim21.authentication.AuthManager;
import com.example.booking_ma_tim21.dto.AccommodationDetailsDTO;
import com.example.booking_ma_tim21.dto.AccommodationPreviewDTO;
import com.example.booking_ma_tim21.dto.ReservationRequestDTO;
import com.example.booking_ma_tim21.model.enumeration.ReservationRequestStatus;
import com.example.booking_ma_tim21.retrofit.AccommodationService;
import com.example.booking_ma_tim21.retrofit.ReservationRequestService;
import com.example.booking_ma_tim21.retrofit.RetrofitService;
import com.example.booking_ma_tim21.util.NavigationSetup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ReservationRequestsActivity extends AppCompatActivity {

    AccommodationService accService;
    AuthManager auth;
    ReservationRequestService resService;
    private EditText searchEditText;
    private Spinner filterSpinner;
    private RecyclerView recyclerView;
    private ReservationRequestsAdapter reservationAdapter;
    Map<Long,String> accNames;
    List<ReservationRequestDTO> reqs=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_requests);


        auth=AuthManager.getInstance(this);

        NavigationSetup.setupNavigation(this, auth);

        RetrofitService ref=new RetrofitService();
        resService=ref.getRetrofit().create(ReservationRequestService.class);
        accService=ref.getRetrofit().create(AccommodationService.class);


        // Initialize views
        searchEditText = findViewById(R.id.searchEditText);
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateRecyclerView();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        filterSpinner = findViewById(R.id.spinnerDataset);
        recyclerView = findViewById(R.id.reservationRequestsRecyclerView);
        accNames=new HashMap<>();

        setReservations();
        setSpinnerInput();

    }
    void setSpinnerInput() {

        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filterSpinner.setAdapter(adapter);
        adapter.add("Status");

        for(ReservationRequestStatus e: ReservationRequestStatus.values()){
            adapter.add(e.toString());
        }

        filterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                updateRecyclerView();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }

        });


    }

    void setNames(){

        for(ReservationRequestDTO req:reqs){

            Call call=accService.getAccommodation(req.getAccommodationId());

            call.enqueue(new Callback<AccommodationDetailsDTO>() {
                @Override
                public void onResponse(Call<AccommodationDetailsDTO> call, Response<AccommodationDetailsDTO> response) {
                    if (response.code() == 200){

                        Log.d("REZ","Meesage recieved");
                        accNames.put(response.body().getId(),response.body().getName());

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

    void updateRecyclerView(){

        String name=searchEditText.getText().toString();
        String status=filterSpinner.getSelectedItem().toString();

        if(status.equals("Status")&&name.equals("")){
            setRecyclerView(reqs);
            return;
        }


        List<ReservationRequestDTO> valids=new ArrayList<>();

        for(ReservationRequestDTO req:reqs){


            if(accNames.get(req.getAccommodationId()).contains(name)){

                if(status.equals("Status")){
                    valids.add(req);
                }
                else if(req.getStatus().name().equals(status)){

                    valids.add(req);


                }

            }


        }

        setRecyclerView(valids);

    }

    private void setReservations() {
        Call call=null;

        if(auth.getUserRole().equals("OWNER")){
             call=resService.getOwnerReservationRequests(auth.getUserIdLong());
        }else{
             call=resService.getUserReservationRequests(auth.getUserIdLong());

        }

        call.enqueue(new Callback<List<ReservationRequestDTO>>() {
            @Override
            public void onResponse(Call<List<ReservationRequestDTO>> call, Response<List<ReservationRequestDTO>> response) {
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
            public void onFailure(Call<List<ReservationRequestDTO>> call, Throwable t) {
                t.printStackTrace();
            }


        });
    }

    void setRecyclerView(List<ReservationRequestDTO> reqs){

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        reservationAdapter = new ReservationRequestsAdapter(this,reqs);
        recyclerView.setAdapter(reservationAdapter);

    }
    @Override
    protected void onPause() {
        super.onPause();
        NavigationSetup.closeDrawer(findViewById(R.id.drawerLayout));
    }
}