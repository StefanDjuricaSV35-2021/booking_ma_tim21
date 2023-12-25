package com.example.booking_ma_tim21.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.booking_ma_tim21.R;
import com.example.booking_ma_tim21.adapter.AmenitiesAdapter;
import com.example.booking_ma_tim21.adapter.PricingAdapter;
import com.example.booking_ma_tim21.authentication.AuthManager;
import com.example.booking_ma_tim21.model.AccommodationPricing;
import com.example.booking_ma_tim21.model.enumeration.Amenity;
import com.example.booking_ma_tim21.retrofit.AuthService;
import com.example.booking_ma_tim21.util.NavigationSetup;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AccommodationCreation extends AppCompatActivity {

    AuthManager authManager;
    private EditText street_input_field;
    private EditText city_input_field;
    private EditText country_input_field;
    private EditText accommodation_name;
    private EditText min_guests;
    private EditText max_guests;
    private EditText cancellation_days;
    private Spinner accommodation_type;
    private RecyclerView amenities;

    private RecyclerView timeslots;
    private AmenitiesAdapter amenitiesAdapter;
    private PricingAdapter pricingAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accommodation_creation);

        authManager = AuthManager.getInstance(getApplicationContext());
        NavigationSetup.setupNavigation(this, authManager);

        street_input_field = findViewById(R.id.street_input_field);
        city_input_field = findViewById(R.id.city_input_field);
        country_input_field = findViewById(R.id.country_input_field);
        accommodation_name = findViewById(R.id.accommodation_name);
        min_guests = findViewById(R.id.min_guests);
        max_guests = findViewById(R.id.max_guests);
        cancellation_days = findViewById(R.id.cancellation_days);

        accommodation_type = findViewById(R.id.accommodation_type);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.accommodation_types, // Create a string array resource with enum values
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        accommodation_type.setAdapter(adapter);

        List<Amenity> amenitiesList = Arrays.asList(Amenity.values());

        amenities = findViewById(R.id.amenities);
        amenities.setLayoutManager(new LinearLayoutManager(this));

        amenitiesAdapter = new AmenitiesAdapter(amenitiesList);
        amenities.setAdapter(amenitiesAdapter);

        List<AccommodationPricing> accommodationPricings = new ArrayList<>();

        pricingAdapter = new PricingAdapter(accommodationPricings);
        timeslots.setAdapter(pricingAdapter);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("log", "onRestart()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        NavigationSetup.closeDrawer(findViewById(R.id.drawerLayout));
    }
}