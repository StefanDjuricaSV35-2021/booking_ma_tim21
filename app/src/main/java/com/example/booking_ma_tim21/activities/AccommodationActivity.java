package com.example.booking_ma_tim21.activities;

import static android.app.PendingIntent.getActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.booking_ma_tim21.R;
import com.example.booking_ma_tim21.adapter.AmenityCheckBoxAdapter;
import com.example.booking_ma_tim21.adapter.AmenityListAdapter;
import com.example.booking_ma_tim21.adapter.ImageAdapter;
import com.example.booking_ma_tim21.authentication.AuthManager;
import com.example.booking_ma_tim21.dto.AccommodationDetailsDTO;
import com.example.booking_ma_tim21.fragments.MapFragment;
import com.example.booking_ma_tim21.fragments.ReservationBarFragment;
import com.example.booking_ma_tim21.model.TimeSlot;
import com.example.booking_ma_tim21.model.enumeration.Amenity;
import com.example.booking_ma_tim21.util.DatePickerCreator;
import com.example.booking_ma_tim21.util.NavigationSetup;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.util.ArrayList;
import java.util.Date;

public class AccommodationActivity extends AppCompatActivity {
    AccommodationDetailsDTO acc;
    Bundle searchParams;
    AuthManager authManager;
    TextView availability;
    MaterialButton ownerReviews;
    MaterialButton accomodationReviews;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accommodation);
        authManager = AuthManager.getInstance(getApplicationContext());
        NavigationSetup.setupNavigation(this, authManager);

        setAccommodation();
        setView();
    }

    @Override
    protected void onPause() {
        super.onPause();
        NavigationSetup.closeDrawer(findViewById(R.id.drawerLayout));
    }

    void setAccommodation(){
        Intent intent=getIntent();
        acc= (AccommodationDetailsDTO) intent.getSerializableExtra("accommodation");
        searchParams=intent.getExtras();
    }

    void setView(){
        ViewPager imageSlider=findViewById(R.id.image_slider);
        TextView name=findViewById(R.id.name_tv);
        TextView location=findViewById(R.id.location_tv);
        TextView type=findViewById(R.id.type_tv);
        TextView guests=findViewById(R.id.guests_tv);
        availability=findViewById(R.id.availability_tv);
        accomodationReviews=findViewById(R.id.acc_reviews_btn);
        ownerReviews=findViewById(R.id.own_reviews_btn);


        setImageSlider(imageSlider);
        name.setText(acc.getName());
        location.setText(acc.getLocation());
        type.setText(acc.getType().toString());
        guests.setText(acc.getMinGuests()+"-"+acc.getMaxGuests()+" Guests");

        setAmenityList((ArrayList<Amenity>) acc.getAmenities());
        setMapFragment();
        setButtonClicks();
        setResFragment();


    }

    void setButtonClicks(){

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

                Bundle b=new Bundle();
                b.putLong("ACCOMMODATION_ID",acc.getId());
                intent.putExtras(b);

                v.getContext().startActivity(intent);

            }

        });

        ownerReviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

     void setResFragment(){

        AuthManager auth=AuthManager.getInstance(this);

        if(!(auth.isLoggedIn()&& auth.getUserRole().equals("GUEST"))){
            return;
        }

        Bundle resRestrictions= new Bundle();
        resRestrictions.putInt("min",acc.getMinGuests());
        resRestrictions.putInt("max",acc.getMaxGuests());
        resRestrictions.putParcelableArrayList("dates",(ArrayList) acc.getDates());

        ReservationBarFragment fragment = ReservationBarFragment.newInstance(searchParams,resRestrictions);

         FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
         transaction.add(R.id.res_bar_frag, fragment, "Res Bar");
         transaction.commit();

    }


    void setImageSlider(ViewPager imageSlider){

        ImageAdapter adapterView = new ImageAdapter(this,acc.getPhotos());
        imageSlider.setAdapter(adapterView);

    }

    void setMapFragment(){
        MapFragment map=new MapFragment();

        Bundle bundle = new Bundle();
        bundle.putString("location", acc.getLocation());

        map.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.map_fragment_container, map)
                .commit();
    }

    void setAmenityList(ArrayList<Amenity> amenities){

        RecyclerView listView=findViewById(R.id.amenities_lv);
        listView.setLayoutManager(new LinearLayoutManager(this));
        AmenityListAdapter adapter = new AmenityListAdapter(amenities); // Replace with your custom adapter
        listView.setAdapter(adapter);

    }
}