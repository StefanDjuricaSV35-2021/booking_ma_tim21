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
import com.example.booking_ma_tim21.dto.AccommodationDetailsDTO;
import com.example.booking_ma_tim21.dto.AccommodationPreviewDTO;
import com.example.booking_ma_tim21.retrofit.AccommodationService;
import com.example.booking_ma_tim21.retrofit.RetrofitService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccommodatioPreviewRecycleViewFragment extends Fragment {

    RecyclerView previewRecycler;
    PreviewAdapter previewAdapter;
    AccommodationService service;
    RelativeLayout loadingPanel;
    String location;
    String[] dates;
    String guests;
    String filter;

    public AccommodatioPreviewRecycleViewFragment() {
        // Required empty public constructor
    }

    public static AccommodatioPreviewRecycleViewFragment newInstance(String location, String guests, String date,String filter) {

        AccommodatioPreviewRecycleViewFragment fragment = new AccommodatioPreviewRecycleViewFragment();
        Bundle args = new Bundle();
        args.putString("location", location);
        args.putString("guests", guests);
        args.putString("date", date);
        args.putString("filter",filter);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getSearchParamsFromArgs();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {return inflater.inflate(R.layout.fragment_accommodatio_preview_recycle_view, container, false);}

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        RetrofitService retrofitService= new RetrofitService();
        service=retrofitService.getRetrofit().create(AccommodationService.class);
        loadingPanel=getView().findViewById(R.id.loadingPanel);
        initializePreviews();

    }

    void getSearchParamsFromArgs(){

        Bundle args=getArguments();
        if(args==null){return;}
        guests=args.getString("guests");
        dates = args.getString("date", "Date").split("/");
        location=args.getString("location");
        filter=args.getString("filter");

    }

    private void initializePreviews(){
        Call call=null;

        Bundle args = getArguments();
        if(args !=null) {

            call=service.getFilteredAccommodations(location,guests,dates[0],dates[1],filter);

        }else {
            call = service.getAllAccommodations();
        }

        enqueuePreviewCall(call);
    }

    void enqueuePreviewCall(Call call){

        call.enqueue(new Callback<List<AccommodationPreviewDTO>>() {
            @Override
            public void onResponse(Call<List<AccommodationPreviewDTO>> call, Response<List<AccommodationPreviewDTO>> response) {
                if (response.code() == 200){

                    Log.d("REZ","Meesage recieved");
                    List<AccommodationPreviewDTO> previewDTOs = response.body();
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

                Call call= service.getAccommodation(preview.getId());
                enqueueDetailsCall(call);

            }
        });
        previewRecycler.setAdapter(previewAdapter);

    }

    void enqueueDetailsCall(Call call){
        call.enqueue(new Callback<AccommodationDetailsDTO>() {
            @Override
            public void onResponse(Call<AccommodationDetailsDTO> call, Response<AccommodationDetailsDTO> response) {
                if (response.code() == 200){

                    Log.d("REZ","Meesage recieved");
                    AccommodationDetailsDTO detailsDTO = response.body();
                    loadingPanel.setVisibility(View.GONE);
                    showDetails(detailsDTO);

                }else{
                    Log.d("REZ","Meesage recieved: "+response.code());
                }
            }

            @Override
            public void onFailure(Call<AccommodationDetailsDTO> call, Throwable t) {
                t.printStackTrace();
            }


        });
    }

    void showDetails(AccommodationDetailsDTO detailsDTO){

        Intent intent=new Intent(getActivity(), AccommodationActivity.class);
        intent.putExtra("accommodation",detailsDTO);

        startActivity(intent);

    }
}