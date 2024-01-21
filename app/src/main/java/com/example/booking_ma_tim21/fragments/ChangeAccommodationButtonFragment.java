package com.example.booking_ma_tim21.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.booking_ma_tim21.R;
import com.example.booking_ma_tim21.activities.AccommodationUpdatingActivity;
import com.example.booking_ma_tim21.dto.AccommodationDetailsDTO;
import com.google.android.material.button.MaterialButton;

public class ChangeAccommodationButtonFragment extends Fragment {

    private AccommodationDetailsDTO detailsDTO;

    public ChangeAccommodationButtonFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_accommodation_button, container, false);

        setButtonClicks(view);

        return view;
    }
    public void setAccommodationDetails(AccommodationDetailsDTO detailsDTO) {
        this.detailsDTO = detailsDTO;
    }
    void setButtonClicks(View view) {
        MaterialButton changeAccommodation = view.findViewById(R.id.change_accommodation_btn);
        changeAccommodation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AccommodationUpdatingActivity.class);
                intent.putExtra("accommodation",detailsDTO);
                startActivity(intent);
            }
        });
    }
}
