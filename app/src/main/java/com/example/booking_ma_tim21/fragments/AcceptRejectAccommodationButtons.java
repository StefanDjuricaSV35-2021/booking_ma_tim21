package com.example.booking_ma_tim21.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.booking_ma_tim21.R;
import com.example.booking_ma_tim21.activities.AccommodationUpdatingActivity;
import com.example.booking_ma_tim21.dto.AccommodationDetailsDTO;
import com.example.booking_ma_tim21.retrofit.AccommodationService;
import com.example.booking_ma_tim21.retrofit.RetrofitService;
import com.example.booking_ma_tim21.retrofit.UserService;
import com.google.android.material.button.MaterialButton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class AcceptRejectAccommodationButtons extends Fragment {

    private AccommodationDetailsDTO detailsDTO;

    private AccommodationService accommodationService;

    public AcceptRejectAccommodationButtons() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_accept_reject_accommodation_buttons, container, false);

        setButtonClicks(view);

        return view;
    }

    public void setAccommodationDetails(AccommodationDetailsDTO detailsDTO) {
        this.detailsDTO = detailsDTO;
        RetrofitService retrofitService = new RetrofitService();
        accommodationService = retrofitService.getRetrofit().create(AccommodationService.class);
    }

    void setButtonClicks(View view) {
        MaterialButton accept = view.findViewById(R.id.accept_accommodation_creation_btn);

        MaterialButton reject = view.findViewById(R.id.reject_accommodation_creation_btn);
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detailsDTO.setEnabled(true);
                Call<AccommodationDetailsDTO> call = accommodationService.updateAccommodation(detailsDTO);

                call.enqueue(new Callback<AccommodationDetailsDTO>() {
                    @Override
                    public void onResponse(Call<AccommodationDetailsDTO> call, Response<AccommodationDetailsDTO> response) {
                        if (response.isSuccessful()) {
                            AccommodationDetailsDTO updatedAccommodation = response.body();
                            Toast.makeText(requireContext(), "Accommodation added", Toast.LENGTH_SHORT).show();
                            if(getActivity()!= null) getActivity().finish();
                        } else {
                            Toast.makeText(requireContext(), "Failed to add accommodation", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<AccommodationDetailsDTO> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
            }
        });

        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call<Void> call = accommodationService.deleteAccommodation(detailsDTO.getId());
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(requireContext(), "Request rejected", Toast.LENGTH_SHORT).show();
                            if(getActivity()!= null) getActivity().finish();
                        } else {
                            Toast.makeText(requireContext(), "Failed to reject request", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
            }
        });
    }
}