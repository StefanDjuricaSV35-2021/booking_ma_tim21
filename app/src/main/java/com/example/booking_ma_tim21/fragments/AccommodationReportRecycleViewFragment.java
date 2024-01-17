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
import com.example.booking_ma_tim21.adapter.UserReportsAdapter;
import com.example.booking_ma_tim21.dto.ReviewReportDTO;
import com.example.booking_ma_tim21.dto.UserReportDTO;
import com.example.booking_ma_tim21.model.UserReportWithEmails;
import com.example.booking_ma_tim21.retrofit.RetrofitService;
import com.example.booking_ma_tim21.retrofit.ReviewReportService;
import com.example.booking_ma_tim21.retrofit.UserReportService;
import com.example.booking_ma_tim21.retrofit.UserService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccommodationReportRecycleViewFragment extends Fragment {
    ReviewReportService reviewReportService;
    RecyclerView accommodationReportRecycler;
    AccommodationReportAdapter accommodationReportAdapter;
    List<ReviewReportDTO> reviewReportDTOS = new ArrayList<>();

    public AccommodationReportRecycleViewFragment() {
        // Required empty public constructor
    }

    public static AccommodationReportRecycleViewFragment newInstance() {
        AccommodationReportRecycleViewFragment fragment = new AccommodationReportRecycleViewFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_accommodation_report_recycle_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RetrofitService retrofitService = new RetrofitService();
        reviewReportService = retrofitService.getRetrofit().create(ReviewReportService.class);
        accommodationReportRecycler = view.findViewById(R.id.accommodation_reports_recycler);
        fetchAccommodationReports();
    }

    private void fetchAccommodationReports() {
        Call<List<ReviewReportDTO>> newCall = reviewReportService.getAccommodationReviewReports();
        newCall.enqueue(new Callback<List<ReviewReportDTO>>() {
            @Override
            public void onResponse(Call<List<ReviewReportDTO>> call, Response<List<ReviewReportDTO>> response) {
                if (response.isSuccessful()) {
                    reviewReportDTOS = response.body();
                    accommodationReportAdapter = new AccommodationReportAdapter(getContext(), reviewReportDTOS);

                    accommodationReportRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
                    accommodationReportRecycler.setAdapter(accommodationReportAdapter);
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