package com.example.booking_ma_tim21.activities;

import static android.app.PendingIntent.getActivity;

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
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.booking_ma_tim21.R;
import com.example.booking_ma_tim21.adapter.AmenityListAdapter;
import com.example.booking_ma_tim21.adapter.ImageAdapter;
import com.example.booking_ma_tim21.authentication.AuthManager;
import com.example.booking_ma_tim21.dto.AccommodationDetailsDTO;
import com.example.booking_ma_tim21.dto.UserDTO;
import com.example.booking_ma_tim21.fragments.ChangeAccommodationButtonFragment;
import com.example.booking_ma_tim21.dto.AccommodationPreviewDTO;
import com.example.booking_ma_tim21.fragments.MapFragment;
import com.example.booking_ma_tim21.fragments.PricingsListFragment;
import com.example.booking_ma_tim21.fragments.ReservationBarFragment;
import com.example.booking_ma_tim21.model.AccommodationPricing;
import com.example.booking_ma_tim21.model.enumeration.Amenity;
import com.example.booking_ma_tim21.retrofit.AccommodationPricingService;
import com.example.booking_ma_tim21.retrofit.RetrofitService;
import com.example.booking_ma_tim21.retrofit.UserService;
import com.example.booking_ma_tim21.retrofit.AccommodationService;
import com.example.booking_ma_tim21.retrofit.FavoriteAccommodationService;
import com.example.booking_ma_tim21.retrofit.RetrofitService;
import com.example.booking_ma_tim21.util.DatePickerCreator;
import com.example.booking_ma_tim21.util.NavigationSetup;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccommodationActivity extends AppCompatActivity {

    Boolean isFavorite = null;

    FavoriteAccommodationService favoriteAccommodationService;
    AccommodationDetailsDTO acc;
    Bundle searchParams;
    AuthManager authManager;
    TextView availability;
    MaterialButton ownerReviews;
    MaterialButton accomodationReviews;

    UserService userService;
    AccommodationPricingService pricingService;
    UserDTO loggedInUser;
    Button favorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accommodation);
        authManager = AuthManager.getInstance(getApplicationContext());
        NavigationSetup.setupNavigation(this, authManager);
        RetrofitService retrofitService = new RetrofitService();
        userService = retrofitService.getRetrofit().create(UserService.class);
        pricingService = retrofitService.getRetrofit().create(AccommodationPricingService.class);
        this.favoriteAccommodationService = retrofitService.getRetrofit().create(FavoriteAccommodationService.class);

        setAccommodation();
        setView();
    }

    @Override
    protected void onPause() {
        super.onPause();
        NavigationSetup.closeDrawer(findViewById(R.id.drawerLayout));
    }

    void setAccommodation() {
        Intent intent = getIntent();
        acc = (AccommodationDetailsDTO) intent.getSerializableExtra("accommodation");
        loggedInUser = (UserDTO) intent.getSerializableExtra("loggedInUser");
        searchParams = intent.getExtras();
    }

    void setView() {
        ViewPager imageSlider = findViewById(R.id.image_slider);
        TextView name = findViewById(R.id.name_tv);
        TextView location = findViewById(R.id.location_tv);
        TextView type = findViewById(R.id.type_tv);
        TextView guests = findViewById(R.id.guests_tv);
        availability = findViewById(R.id.availability_tv);
        accomodationReviews = findViewById(R.id.acc_reviews_btn);
        ownerReviews = findViewById(R.id.own_reviews_btn);
        favorite = findViewById(R.id.favorite_btn);

        setImageSlider(imageSlider);
        name.setText(acc.getName());
        location.setText(acc.getLocation());
        type.setText(acc.getType().toString());
        guests.setText(acc.getMinGuests() + "-" + acc.getMaxGuests() + " Guests");

        setAmenityList((ArrayList<Amenity>) acc.getAmenities());
        setMapFragment();
        setFavoriteButton();
        setButtonClicks();
        setResFragment();
        setChangeAccommodationFragmentAndPricings();

    }

    void setButtonClicks() {

        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFavoriteButtonBackground(!isFavorite);
            }
        });

        availability.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MaterialDatePicker picker = DatePickerCreator.getDatePicker(acc.getDates());
                picker.show(getSupportFragmentManager(), picker.toString());

            }
        });

        accomodationReviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), AccommodationReviewPage.class);

                Bundle b = new Bundle();
                b.putLong("ACCOMMODATION_ID", acc.getId());
                intent.putExtras(b);

                v.getContext().startActivity(intent);

            }

        });

        ownerReviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), GuestOwnerReview.class);

                Bundle b = new Bundle();
                b.putString("OWNER_ID", authManager.getUserId());
                intent.putExtras(b);

                v.getContext().startActivity(intent);

            }
        });

    }

    void setFavoriteButton() {

        AuthManager auth = AuthManager.getInstance(this);
        if (!(auth.isLoggedIn() && auth.getUserRole().equals("GUEST"))) {
            this.favorite.setVisibility(View.GONE);
            return;
        } else {
            checkIfFavorite();
        }
    }

    void checkIfFavorite() {

        AuthManager auth = AuthManager.getInstance(this);
        Long userId = Long.valueOf(auth.getUserId());

        Call call = favoriteAccommodationService.isUsersFavorite(acc.getId(), userId);

        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.code() == 200) {

                    Log.d("REZ", "Meesage recieved");
                    Boolean isFavorite = response.body();
                    setFavoriteButtonBackground(isFavorite);

                } else {
                    Log.d("REZ", "Meesage recieved: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }

    void setFavoriteButtonBackground(Boolean isFavorite) {

        if (isFavorite) {
            this.favorite.setBackgroundResource(R.drawable.favorite_unselect_24px);
        } else {
            this.favorite.setBackgroundResource(R.drawable.favorite_select_24px);
        }

        this.isFavorite = isFavorite;

    }

    void setRatingBar() {

        RatingBar ratingBar = findViewById(R.id.rating_bar);
        ratingBar.setMax(5);

    }

    void setResFragment() {

        AuthManager auth = AuthManager.getInstance(this);

        if (!(auth.isLoggedIn() && auth.getUserRole().equals("GUEST"))) {
            return;
        }

        Bundle resRestrictions = new Bundle();
        resRestrictions.putInt("min", acc.getMinGuests());
        resRestrictions.putInt("max", acc.getMaxGuests());
        resRestrictions.putParcelableArrayList("dates", (ArrayList) acc.getDates());

        ReservationBarFragment fragment = ReservationBarFragment.newInstance(searchParams, resRestrictions);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.res_bar_frag, fragment, "Res Bar");
        transaction.commit();

    }

    void setChangeAccommodationFragmentAndPricings() {

        AuthManager auth = AuthManager.getInstance(this);

        if (!(auth.isLoggedIn() && auth.getUserRole().equals("OWNER"))) {
            return;
        }

        String email = auth.getUserId();
        Call<UserDTO> call = this.userService.getUser(email);
        call.enqueue(new Callback<UserDTO>() {
            @Override
            public void onResponse(Call<UserDTO> call, Response<UserDTO> response) {
                if (response.isSuccessful()) {
                    loggedInUser = response.body();
                    if (loggedInUser == null)
                        return;
                    if (!loggedInUser.getId().equals(acc.getOwnerId()))
                        return;

                    Call<List<AccommodationPricing>> pricingsCall = pricingService
                            .getPricingsForAccommodation(acc.getId());

                    pricingsCall.enqueue(new Callback<List<AccommodationPricing>>() {
                        @Override
                        public void onResponse(Call<List<AccommodationPricing>> pricingsCall,
                                Response<List<AccommodationPricing>> response) {
                            if (response.isSuccessful()) {
                                List<AccommodationPricing> pricings = response.body();
                                PricingsListFragment fragment = new PricingsListFragment();
                                fragment.setPricings(pricings);
                                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                                transaction.replace(R.id.pricings_container, fragment);
                                transaction.commit();
                            } else {
                                Log.d("REZ", "Message received: " + response.code());
                            }
                        }

                        @Override
                        public void onFailure(Call<List<AccommodationPricing>> pricingsCall, Throwable t) {
                            t.printStackTrace();
                        }
                    });

                    ChangeAccommodationButtonFragment fragment = new ChangeAccommodationButtonFragment();
                    fragment.setAccommodationDetails(acc);
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.change_accommodation_btn_container, fragment);
                    transaction.commit();
                } else {
                    Log.d("REZ", "Message received: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<UserDTO> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    void setImageSlider(ViewPager imageSlider) {

        ImageAdapter adapterView = new ImageAdapter(this, acc.getPhotos());
        imageSlider.setAdapter(adapterView);

    }

    void setMapFragment() {
        MapFragment map = new MapFragment();

        Bundle bundle = new Bundle();
        bundle.putString("location", acc.getLocation());

        map.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.map_fragment_container, map)
                .commit();
    }

    void setAmenityList(ArrayList<Amenity> amenities) {

        RecyclerView listView = findViewById(R.id.amenities_lv);
        listView.setLayoutManager(new LinearLayoutManager(this));
        AmenityListAdapter adapter = new AmenityListAdapter(amenities); // Replace with your custom adapter
        listView.setAdapter(adapter);

    }
}