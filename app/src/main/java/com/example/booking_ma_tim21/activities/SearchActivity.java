package com.example.booking_ma_tim21.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.booking_ma_tim21.R;
import com.example.booking_ma_tim21.adapter.PreviewAdapter;
import com.example.booking_ma_tim21.fragments.AccommodatioPreviewRecycleViewFragment;
import com.example.booking_ma_tim21.fragments.AccommodationFilterFragment;
import com.example.booking_ma_tim21.fragments.SearchClosedFragment;
import com.example.booking_ma_tim21.fragments.SearchFragment;

public class SearchActivity extends AppCompatActivity implements SearchClosedFragment.SearchOpenListener {


    Button filter;
    Button search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initiateClosedSearchFragment();
        initiateRecycleView();


        filter=findViewById(R.id.filter_btn);

        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AccommodationFilterFragment().show(getSupportFragmentManager(),"filter");
            }
        });



    }

    void initiateRecycleView(){

        Intent intent=getIntent();
        String guests=intent.getStringExtra("guests");
        String date= intent.getStringExtra("date");
        String location=intent.getStringExtra("location");

        Fragment fragment= AccommodatioPreviewRecycleViewFragment.newInstance(location,guests,date);
        FragmentManager fragmentManager = getSupportFragmentManager();;
        FragmentTransaction fragmentTransaction = fragmentManager
                .beginTransaction();

        fragmentTransaction.add(R.id.preview_recycler_fragment, fragment, null);
        fragmentTransaction.commit();


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

        float density =getResources().getDisplayMetrics().density;
        int px = Math.round(310 * density);

        Fragment fragment= new SearchFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();;
        FragmentTransaction fragmentTransaction = fragmentManager
                .beginTransaction()
                .addSharedElement(findViewById(R.id.search_bg_ll),"openBg")
                .addSharedElement(findViewById(R.id.search_btn),"openSearch");

        fragmentTransaction.replace(R.id.search_fragment, fragment);
        fragmentTransaction.commit();




    }
}