package com.example.booking_ma_tim21.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.transition.Scene;
import android.widget.Button;
import android.widget.EditText;

import com.example.booking_ma_tim21.R;
import com.example.booking_ma_tim21.adapter.PreviewAdapter;
import com.example.booking_ma_tim21.fragments.SearchClosedFragment;
import com.example.booking_ma_tim21.fragments.SearchOpenFragment;
import com.example.booking_ma_tim21.model.AccommodationPreview;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements SearchClosedFragment.SearchOpenListener {

    RecyclerView previewRecycler;
    PreviewAdapter previewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initiateClosedSearchFragment();

    }

    void initiateClosedSearchFragment(){

        Intent intent=getIntent();
        String guests=intent.getStringExtra("guests");
        String date= intent.getStringExtra("date");
        String location=intent.getStringExtra("location");

        Fragment fragment= SearchClosedFragment.newInstance(location,guests,date);
        FragmentManager fragmentManager = getSupportFragmentManager();;
        FragmentTransaction fragmentTransaction = fragmentManager
                .beginTransaction();

        fragmentTransaction.add(R.id.search_fragment, fragment, null);
        fragmentTransaction.commit();
    }

    @Override
    public void openSearch() {

        Fragment fragment= new SearchOpenFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();;
        FragmentTransaction fragmentTransaction = fragmentManager
                .beginTransaction()
                .addSharedElement(findViewById(R.id.search_closed_bg_ll),"openBg")
                .addSharedElement(findViewById(R.id.search_btn),"openSearch");
        fragmentTransaction.replace(R.id.search_fragment, fragment);
        fragmentTransaction.commit();
    }
}