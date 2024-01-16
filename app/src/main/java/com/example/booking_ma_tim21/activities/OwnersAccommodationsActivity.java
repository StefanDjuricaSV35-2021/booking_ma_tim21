package com.example.booking_ma_tim21.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.booking_ma_tim21.R;
import com.example.booking_ma_tim21.authentication.AuthManager;
import com.example.booking_ma_tim21.fragments.AccommodatioPreviewRecycleViewFragment;
import com.example.booking_ma_tim21.util.NavigationSetup;

public class OwnersAccommodationsActivity extends AppCompatActivity {
    AuthManager authManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owners_accommodations);
        authManager = AuthManager.getInstance(getApplicationContext());
        NavigationSetup.setupNavigation(this, authManager);


        AccommodatioPreviewRecycleViewFragment fragment = AccommodatioPreviewRecycleViewFragment.newInstance(null, null, null, null, true);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.preview_recycler_fragment, fragment);
        transaction.commit();
    }

    @Override
    protected void onPause() {
        super.onPause();
        NavigationSetup.closeDrawer(findViewById(R.id.drawerLayout));
    }

    public AuthManager getAuthManager() {
        return authManager;
    }
}