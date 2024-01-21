package com.example.booking_ma_tim21.fragments;

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

import com.example.booking_ma_tim21.R;
import com.example.booking_ma_tim21.adapter.AccommodationReportAdapter;
import com.example.booking_ma_tim21.adapter.OwnerReportAdapter;
import com.example.booking_ma_tim21.dto.ReviewReportDTO;
import com.example.booking_ma_tim21.retrofit.RetrofitService;
import com.example.booking_ma_tim21.retrofit.ReviewReportService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OwnerReportRecycleViewFragment extends Fragment {

    ReviewReportService reviewReportService;
    RecyclerView ownerReportRecycler;
    OwnerReportAdapter ownerReportAdapter;
    List<ReviewReportDTO> reviewReportDTOS = new ArrayList<>();
    public OwnerReportRecycleViewFragment() {
        // Required empty public constructor
    }

    public static OwnerReportRecycleViewFragment newInstance() {
        OwnerReportRecycleViewFragment fragment = new OwnerReportRecycleViewFragment();

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
        return inflater.inflate(R.layout.fragment_owner_report_recycle_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RetrofitService retrofitService = new RetrofitService();
        reviewReportService = retrofitService.getRetrofit().create(ReviewReportService.class);
        ownerReportRecycler = view.findViewById(R.id.owner_reports_recycler);
        fetchAccommodationReports();
    }

    private void fetchAccommodationReports() {
        Call<List<ReviewReportDTO>> newCall = reviewReportService.getOwnerReviewReports();
        newCall.enqueue(new Callback<List<ReviewReportDTO>>() {
            @Override
            public void onResponse(Call<List<ReviewReportDTO>> call, Response<List<ReviewReportDTO>> response) {
                if (response.isSuccessful()) {
                    reviewReportDTOS = response.body();
                    ownerReportAdapter = new OwnerReportAdapter(getContext(), reviewReportDTOS);

                    ownerReportRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
                    ownerReportRecycler.setAdapter(ownerReportAdapter);
                } else {
                    Log.d("REZ", "Message received: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<ReviewReportDTO>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}