package com.example.booking_ma_tim21.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.booking_ma_tim21.R;
import com.example.booking_ma_tim21.activities.AccommodationActivity;
import com.example.booking_ma_tim21.activities.MainActivity;
import com.example.booking_ma_tim21.adapter.PreviewAdapter;
import com.example.booking_ma_tim21.model.AccommodationPreview;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccommodatioPreviewRecycleViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccommodatioPreviewRecycleViewFragment extends Fragment {

    RecyclerView previewRecycler;
    PreviewAdapter previewAdapter;

    public AccommodatioPreviewRecycleViewFragment() {
        // Required empty public constructor
    }

    public static AccommodatioPreviewRecycleViewFragment newInstance(String param1, String param2) {
        AccommodatioPreviewRecycleViewFragment fragment = new AccommodatioPreviewRecycleViewFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_accommodatio_preview_recycle_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

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

        previewRecycler = getView().findViewById(R.id.preview_recycler);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);
        previewRecycler.setLayoutManager(layoutManager);
        previewAdapter= new PreviewAdapter(getContext(), accommodationPreview, new PreviewAdapter.ItemClickListener() {
            @Override
            public void onItemClick(AccommodationPreview preview) {

                Intent intent=createIntent(preview);

                startActivity(intent);

            }
        });
        previewRecycler.setAdapter(previewAdapter);

    }

    Intent createIntent(AccommodationPreview preview){

        String name=preview.getName();
        String price=preview.getPrice();
        String location=preview.getLocation();
        int imageUrl=preview.getImageUrl();

        Intent intent=new Intent(getActivity(), AccommodationActivity.class);

        intent.putExtra("name",name);
        intent.putExtra("price",price);
        intent.putExtra("location",location);
        intent.putExtra("image",imageUrl);

        return intent;

    }
}