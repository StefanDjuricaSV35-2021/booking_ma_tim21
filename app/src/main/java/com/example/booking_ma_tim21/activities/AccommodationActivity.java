package com.example.booking_ma_tim21.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.booking_ma_tim21.R;
import com.example.booking_ma_tim21.model.AccommodationPreview;
import com.example.booking_ma_tim21.util.NavigationSetup;

public class AccommodationActivity extends AppCompatActivity {

    AccommodationPreview accommodationPreview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accommodation);
        NavigationSetup.setupNavigation(this);

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

        String name=intent.getStringExtra("name");
        String price= intent.getStringExtra("price");
        String location=intent.getStringExtra("location");
        int imageUrl=intent.getIntExtra("image",0);

        this.accommodationPreview= new AccommodationPreview(name,location,price,imageUrl);
    }

    void setView(){
        ImageView image=findViewById(R.id.image);
        TextView name=findViewById(R.id.name_tv);
        TextView location=findViewById(R.id.location_tv);
        TextView price=findViewById(R.id.price_tv);

        image.setImageResource(accommodationPreview.getImageUrl());
        name.setText(accommodationPreview.getName());
        location.setText(accommodationPreview.getLocation());
        price.setText(accommodationPreview.getPrice());

    }
}