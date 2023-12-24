package com.example.booking_ma_tim21.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.booking_ma_tim21.R;
import com.example.booking_ma_tim21.authentication.AuthManager;
import com.example.booking_ma_tim21.model.AccommodationPreview;
import com.example.booking_ma_tim21.util.NavigationSetup;

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

        this.accommodationPreview= new AccommodationPreview(name,location,imageSrc);
    }

    void setView(){
        ImageView image=findViewById(R.id.image);
        TextView name=findViewById(R.id.name_tv);
        TextView location=findViewById(R.id.location_tv);
        TextView price=findViewById(R.id.price_tv);

        Bitmap bMap = BitmapFactory.decodeFile(accommodationPreview.getImageSrc());
        image.setImageBitmap(bMap);
        name.setText(accommodationPreview.getName());
        location.setText(accommodationPreview.getLocation());

    }
}