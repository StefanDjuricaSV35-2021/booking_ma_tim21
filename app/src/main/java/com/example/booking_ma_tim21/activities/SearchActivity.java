package com.example.booking_ma_tim21.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.transition.Scene;

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
        initializePreviews();

    }

    private void initializePreviews(){
        List<AccommodationPreview> previews=new ArrayList<>();

        previews.add(new AccommodationPreview("AAA","Beograd","1",R.drawable.apt_img));
        previews.add(new AccommodationPreview("BBB","Novi Sad","2",R.drawable.apt_img));
        previews.add(new AccommodationPreview("CCC","Nis","3",R.drawable.apt_img));
        previews.add(new AccommodationPreview("DDD","Kragujevac","4",R.drawable.apt_img));
        previews.add(new AccommodationPreview("EEE","Leskovac","5",R.drawable.apt_img));

        setPreviewRecycler(previews);
    }

    private void setPreviewRecycler(List<AccommodationPreview> accommodationPreview){

        previewRecycler = findViewById(R.id.preview_recycler);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        previewRecycler.setLayoutManager(layoutManager);
        previewAdapter= new PreviewAdapter(this,accommodationPreview);
        previewRecycler.setAdapter(previewAdapter);

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