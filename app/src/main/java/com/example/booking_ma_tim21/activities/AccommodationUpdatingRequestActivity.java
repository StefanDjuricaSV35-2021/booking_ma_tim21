package com.example.booking_ma_tim21.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.booking_ma_tim21.R;
import com.example.booking_ma_tim21.adapter.AmenityListAdapter;
import com.example.booking_ma_tim21.adapter.ImageAdapter;
import com.example.booking_ma_tim21.authentication.AuthManager;
import com.example.booking_ma_tim21.dto.AccommodationChangeRequestDTO;
import com.example.booking_ma_tim21.dto.AccommodationDetailsDTO;
import com.example.booking_ma_tim21.dto.AccommodationPricingChangeRequestDTO;
import com.example.booking_ma_tim21.dto.UserDTO;
import com.example.booking_ma_tim21.fragments.MapFragment;
import com.example.booking_ma_tim21.fragments.PricingsListFragment;
import com.example.booking_ma_tim21.model.AccommodationPricing;
import com.example.booking_ma_tim21.model.enumeration.Amenity;
import com.example.booking_ma_tim21.retrofit.AccommodationChangeRequestService;
import com.example.booking_ma_tim21.retrofit.AccommodationPricingChangeRequestService;
import com.example.booking_ma_tim21.retrofit.AccommodationPricingService;
import com.example.booking_ma_tim21.retrofit.AccommodationService;
import com.example.booking_ma_tim21.retrofit.FavoriteAccommodationService;
import com.example.booking_ma_tim21.retrofit.RetrofitService;
import com.example.booking_ma_tim21.retrofit.UserService;
import com.example.booking_ma_tim21.util.NavigationSetup;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.divider.MaterialDivider;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccommodationUpdatingRequestActivity extends AppCompatActivity {
    AccommodationChangeRequestDTO changeRequestDTO;
    List<AccommodationPricingChangeRequestDTO> pricingRequests;
    AuthManager authManager;

    AccommodationChangeRequestService accommodationChangeRequestService;
    AccommodationPricingChangeRequestService pricingChangeRequestService;
    AccommodationService accommodationService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accommodation_updating_request);

        authManager = AuthManager.getInstance(getApplicationContext());
        NavigationSetup.setupNavigation(this, authManager);
        RetrofitService retrofitService = new RetrofitService();
        accommodationChangeRequestService = retrofitService.getRetrofit().create(AccommodationChangeRequestService.class);
        pricingChangeRequestService = retrofitService.getRetrofit().create(AccommodationPricingChangeRequestService.class);
        accommodationService = retrofitService.getRetrofit().create(AccommodationService.class);

        setAccommodation();
        setView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setAccommodation();
    }
    @Override
    protected void onPause() {
        super.onPause();
        NavigationSetup.closeDrawer(findViewById(R.id.drawerLayout));
    }

    void setAccommodation() {
        Intent intent = getIntent();
        changeRequestDTO = (AccommodationChangeRequestDTO) intent.getParcelableExtra("accommodationChangeRequest");
    }

    void setView() {
        AuthManager auth = AuthManager.getInstance(this);


        ViewPager imageSlider = findViewById(R.id.image_slider_updating);
        TextView name = findViewById(R.id.name_tv_updating);
        TextView location = findViewById(R.id.location_tv_updating);
        TextView type = findViewById(R.id.type_tv_updating);
        TextView guests = findViewById(R.id.guests_tv_updating);

        setImageSlider(imageSlider);
        name.setText(changeRequestDTO.getName());
        location.setText(changeRequestDTO.getLocation());
        type.setText(changeRequestDTO.getType().toString());
        guests.setText(changeRequestDTO.getMinGuests() + "-" + changeRequestDTO.getMaxGuests() + " Guests");

        setAmenityList((ArrayList<Amenity>) changeRequestDTO.getAmenities());
        setMapFragment();
        setUpPricings();
        setAcceptRejectChangesButtons();

    }

    private void setAcceptRejectChangesButtons() {
        MaterialButton accept = findViewById(R.id.accept_updating_request);

        MaterialButton reject = findViewById(R.id.reject_updating_request);
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AccommodationDetailsDTO accommodation = new AccommodationDetailsDTO();
                accommodation.setId(changeRequestDTO.getAccommodationId());
                accommodation.setOwnerId(changeRequestDTO.getOwnerId());
                accommodation.setName(changeRequestDTO.getName());
                accommodation.setType(changeRequestDTO.getType());
                accommodation.setMinGuests(changeRequestDTO.getMinGuests());
                accommodation.setMaxGuests(changeRequestDTO.getMaxGuests());
                accommodation.setDescription(changeRequestDTO.getDescription());
                accommodation.setAmenities(changeRequestDTO.getAmenities());
                accommodation.setPhotos(changeRequestDTO.getPhotos());
                accommodation.setDaysForCancellation(changeRequestDTO.getDaysForCancellation());
                accommodation.setEnabled(true);
                accommodation.setPerNight(changeRequestDTO.isPerNight());
                accommodation.setAutoAccepting(changeRequestDTO.isAutoAccepting());
                accommodation.setLocation(changeRequestDTO.getLocation());
                Call<AccommodationDetailsDTO> call = accommodationService.updateAccommodation(accommodation);
                call.enqueue(new Callback<AccommodationDetailsDTO>() {
                    @Override
                    public void onResponse(Call<AccommodationDetailsDTO> call, Response<AccommodationDetailsDTO> response) {
                        if (response.isSuccessful()) {
                            AccommodationDetailsDTO updatedAccommodation = response.body();
                            Log.d("REZ", "Message received: " + response.code());
                        } else {
                            Log.d("REZ", "Message received: " + response.code());
                        }
                    }
                    @Override
                    public void onFailure(Call<AccommodationDetailsDTO> call, Throwable t) {
                        t.printStackTrace();
                    }
                });

                Call<Void> newCall = pricingChangeRequestService.updateAccommodationPricings(changeRequestDTO.getAccommodationId(), pricingRequests);

                newCall.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            Log.d("REZ", "Message received: " + response.code());
                        } else {
                            Log.d("REZ", "Message received: " + response.code());
                        }
                    }
                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
                Call<Void> callDelete = accommodationChangeRequestService.deleteAccommodationChangeRequest(changeRequestDTO.getId());
                callDelete.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Accommodation updated", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "Failed to update accommodation", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        t.printStackTrace();
                    }
                });

            }
        });

        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call<Void> call = accommodationChangeRequestService.deleteAccommodationChangeRequest(changeRequestDTO.getId());
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Request rejected", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "Failed to reject request", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
            }
        });
    }


    private void setUpPricings() {
        AuthManager auth = AuthManager.getInstance(this);

        if (!(auth.isLoggedIn() || auth.getUserRole().equals("ADMIN"))) {
            return;
        }

        Call<List<AccommodationPricingChangeRequestDTO>> pricingsCall = pricingChangeRequestService
                .getAllAccommodationPricingChangeRequests(changeRequestDTO.getId());

        pricingsCall.enqueue(new Callback<List<AccommodationPricingChangeRequestDTO>>() {
            @Override
            public void onResponse(Call<List<AccommodationPricingChangeRequestDTO>> pricingsCall,
                                   Response<List<AccommodationPricingChangeRequestDTO>> response) {
                if (response.isSuccessful()) {
                    List<AccommodationPricingChangeRequestDTO> pricingChangeRequests = response.body();
                    if (pricingChangeRequests == null) return;
                    pricingRequests = pricingChangeRequests;
                    List<AccommodationPricing> pricings = new ArrayList<>();
                    for (AccommodationPricingChangeRequestDTO p : pricingChangeRequests) {
                        AccommodationPricing pricing = new AccommodationPricing(p.getId(), p.getAccommodationId(), p.getTimeSlot(), p.getPrice());
                        pricings.add(pricing);
                    }

                    PricingsListFragment fragment = new PricingsListFragment();
                    fragment.setPricings(pricings);
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.pricings_container_updating, fragment);
                    transaction.commit();
                } else {
                    Log.d("REZ", "Message received: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<AccommodationPricingChangeRequestDTO>> pricingsCall, Throwable t) {
                t.printStackTrace();
            }
        });


    }



    void setImageSlider(ViewPager imageSlider) {

        ImageAdapter adapterView = new ImageAdapter(this, changeRequestDTO.getPhotos());
        imageSlider.setAdapter(adapterView);

    }

    void setMapFragment() {
        MapFragment map = new MapFragment();

        Bundle bundle = new Bundle();
        bundle.putString("location", changeRequestDTO.getLocation());

        map.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.map_fragment_container_updating, map)
                .commit();
    }

    void setAmenityList(ArrayList<Amenity> amenities) {

        RecyclerView listView = findViewById(R.id.amenities_lv_updating);
        listView.setLayoutManager(new LinearLayoutManager(this));
        AmenityListAdapter adapter = new AmenityListAdapter(amenities); // Replace with your custom adapter
        listView.setAdapter(adapter);

    }
}