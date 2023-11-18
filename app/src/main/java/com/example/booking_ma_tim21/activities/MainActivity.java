package com.example.booking_ma_tim21.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.example.booking_ma_tim21.R;
import com.example.booking_ma_tim21.util.NavigationSetup;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NavigationSetup.setupNavigation(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        NavigationSetup.closeDrawer(findViewById(R.id.drawerLayout));
    }
}