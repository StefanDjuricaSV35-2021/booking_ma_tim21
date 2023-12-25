package com.example.booking_ma_tim21.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.booking_ma_tim21.R;
import com.example.booking_ma_tim21.activities.AccommodationActivity;
import com.example.booking_ma_tim21.adapter.PreviewAdapter;
import com.example.booking_ma_tim21.dto.AccommodationPreviewDTO;
import com.example.booking_ma_tim21.retrofit.AccommodationService;
import com.example.booking_ma_tim21.retrofit.RetrofitService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccommodatioPreviewRecycleViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccommodatioPreviewRecycleViewFragment extends Fragment {

    RecyclerView previewRecycler;
    PreviewAdapter previewAdapter;
    AccommodationService service;
    RelativeLayout loadingPanel;
    String location;
    String date;
    String guests;




    public AccommodatioPreviewRecycleViewFragment() {
        // Required empty public constructor
    }

    public static AccommodatioPreviewRecycleViewFragment newInstance(String location, String guests, String date) {

        AccommodatioPreviewRecycleViewFragment fragment = new AccommodatioPreviewRecycleViewFragment();
        Bundle args = new Bundle();
        args.putString("location", location);
        args.putString("guests", guests);
        args.putString("date", date);

        fragment.setArguments(args);
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
        View view= inflater.inflate(R.layout.fragment_accommodatio_preview_recycle_view, container, false);
        Bundle args = getArguments();
        if(args !=null) {
            this.location = args.getString("location", "Location");
            this.guests = args.getString("guests", "Guests");
            this.date = args.getString("date", "Date");
        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        RetrofitService retrofitService= new RetrofitService();
        service=retrofitService.getRetrofit().create(AccommodationService.class);
        loadingPanel=getView().findViewById(R.id.loadingPanel);
        initializePreviews();

    }

    private void initializePreviews(){
        Bundle args = getArguments();
        Call call=service.getAllAccommodations();

        call.enqueue(new Callback<List<AccommodationPreviewDTO>>() {
            @Override
            public void onResponse(Call<List<AccommodationPreviewDTO>> call, Response<List<AccommodationPreviewDTO>> response) {
                if (response.code() == 200){
                    Log.d("REZ","Meesage recieved");
                    System.out.println(response.body());
                    List<AccommodationPreviewDTO> previewDTOs = response.body();
                    System.out.println(previewDTOs);
                    loadingPanel.setVisibility(View.GONE);
                    setPreviewRecycler(previewDTOs);


                }else{
                    Log.d("REZ","Meesage recieved: "+response.code());
                }
            }

            @Override
            public void onFailure(Call<List<AccommodationPreviewDTO>> call, Throwable t) {
                t.printStackTrace();
            }


        });

    }

    private void setPreviewRecycler(List<AccommodationPreviewDTO> accommodationPreviewDTOs){

        previewRecycler = getView().findViewById(R.id.preview_recycler);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);
        previewRecycler.setLayoutManager(layoutManager);
        previewAdapter= new PreviewAdapter(getContext(), accommodationPreviewDTOs, new PreviewAdapter.ItemClickListener() {
            @Override
            public void onItemClick(AccommodationPreviewDTO preview) {

                Intent intent=createIntent(preview);

                startActivity(intent);

            }
        });
        previewRecycler.setAdapter(previewAdapter);

    }

    Intent createIntent(AccommodationPreviewDTO preview){

        String name=preview.getName();
        String location=preview.getLocation();
        String imageSrc=preview.getImage();

        Intent intent=new Intent(getActivity(), AccommodationActivity.class);

        intent.putExtra("name",name);
        intent.putExtra("location",location);
        intent.putExtra("image",imageSrc);

        return intent;

    }
}