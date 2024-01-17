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
import com.example.booking_ma_tim21.activities.AccommodationUpdatingRequestActivity;
import com.example.booking_ma_tim21.adapter.UpdatingPreviewAdapter;
import com.example.booking_ma_tim21.dto.AccommodationChangeRequestDTO;
import com.example.booking_ma_tim21.retrofit.AccommodationChangeRequestService;
import com.example.booking_ma_tim21.retrofit.RetrofitService;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccommodationUpdatingPreviewRecycleViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccommodationUpdatingPreviewRecycleViewFragment extends Fragment {

    List<AccommodationChangeRequestDTO> previews;
    RecyclerView previewRecycler;
    UpdatingPreviewAdapter previewAdapter;
    AccommodationChangeRequestService service;

    RelativeLayout loadingPanel;

    public AccommodationUpdatingPreviewRecycleViewFragment() {
        // Required empty public constructor
    }

    public static AccommodationUpdatingPreviewRecycleViewFragment newInstance(ArrayList<AccommodationChangeRequestDTO> previews) {
        AccommodationUpdatingPreviewRecycleViewFragment fragment = new AccommodationUpdatingPreviewRecycleViewFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList("previews",previews);
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
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_accommodation_updating_preview_recycle_view, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        RetrofitService retrofitService= new RetrofitService();
        service=retrofitService.getRetrofit().create(AccommodationChangeRequestService.class);

        initializePreviews();

    }

    void getSearchParamsFromArgs(){

        Bundle args=getArguments();
        if(args==null){return;}
        this.previews=args.getParcelableArrayList("previews");
    }

    private void initializePreviews(){
        setPreviewRecycler(this.previews);
    }

    private void setPreviewRecycler(List<AccommodationChangeRequestDTO> accommodationChangeRequestDTOS) {
        previewRecycler = getView().findViewById(R.id.updating_preview_recycler);

        if (accommodationChangeRequestDTOS != null && !accommodationChangeRequestDTOS.isEmpty()) {
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
            previewRecycler.setLayoutManager(layoutManager);
            previewAdapter = new UpdatingPreviewAdapter(getContext(), accommodationChangeRequestDTOS, new UpdatingPreviewAdapter.ItemClickListener() {
                @Override
                public void onItemClick(AccommodationChangeRequestDTO preview) {
                    Call call = service.getAccommodationChangeRequest(preview.getId());
                    enqueueDetailsCall(call);
                }
            });
            previewRecycler.setAdapter(previewAdapter);
        } else {
            previewRecycler.setVisibility(View.GONE);
        }
    }

    void enqueueDetailsCall(Call call){
        call.enqueue(new Callback<AccommodationChangeRequestDTO>() {
            @Override
            public void onResponse(Call<AccommodationChangeRequestDTO> call, Response<AccommodationChangeRequestDTO> response) {
                if (response.code() == 200){

                    Log.d("REZ","Meesage recieved");
                    AccommodationChangeRequestDTO accommodationChangeRequestDTO = response.body();
                    if (loadingPanel != null) {
                        loadingPanel.setVisibility(View.GONE);
                    }
                    showDetails(accommodationChangeRequestDTO);

                }else{
                    Log.d("REZ","Meesage recieved: "+response.code());
                }
            }

            @Override
            public void onFailure(Call<AccommodationChangeRequestDTO> call, Throwable t) {
                t.printStackTrace();
            }


        });
    }

    void showDetails(AccommodationChangeRequestDTO accommodationChangeRequestDTO){

        Intent intent=new Intent(getActivity(), AccommodationUpdatingRequestActivity.class);
        intent.putExtra("accommodationChangeRequest", accommodationChangeRequestDTO);
        startActivity(intent);

    }
}