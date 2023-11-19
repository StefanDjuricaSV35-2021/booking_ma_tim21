package com.example.booking_ma_tim21.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.LayoutTransition;
import android.opengl.Visibility;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.Fade;
import android.transition.TransitionManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.booking_ma_tim21.R;

import com.example.booking_ma_tim21.util.NavigationSetup;

import com.example.booking_ma_tim21.adapter.PreviewAdapter;
import com.example.booking_ma_tim21.model.AccommodationPreview;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    RecyclerView previewRecycler;
    PreviewAdapter previewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializePreviews();

        NavigationSetup.setupNavigation(this);

    }

    @Override
    protected void onPause() {
        super.onPause();
        NavigationSetup.closeDrawer(findViewById(R.id.drawerLayout));
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
}