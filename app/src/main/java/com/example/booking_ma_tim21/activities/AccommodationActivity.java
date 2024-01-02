package com.example.booking_ma_tim21.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.booking_ma_tim21.R;
import com.example.booking_ma_tim21.authentication.AuthManager;
import com.example.booking_ma_tim21.model.AccommodationPreview;
import com.example.booking_ma_tim21.util.NavigationSetup;
import com.squareup.picasso.Picasso;

public class AccommodationActivity extends AppCompatActivity {

    AccommodationPreview accommodationPreview;
    AuthManager authManager;
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

        String name=intent.getStringExtra("name");
        String location=intent.getStringExtra("location");
        String imageSrc=intent.getStringExtra("image");
        Double price=intent.getDoubleExtra("price",0);

        this.accommodationPreview= new AccommodationPreview(name,location,imageSrc,price);
    }

    void setView(){
        ImageView image=findViewById(R.id.image);
        TextView name=findViewById(R.id.name_tv);
        TextView location=findViewById(R.id.location_tv);
        TextView price=findViewById(R.id.price_tv);

        Picasso.get().load("http://10.0.2.2:8080/images/"+accommodationPreview.getImageSrc()).into(image);
        name.setText(accommodationPreview.getName());
        location.setText(accommodationPreview.getLocation());

    }
}