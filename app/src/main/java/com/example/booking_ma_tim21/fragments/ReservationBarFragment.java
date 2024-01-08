package com.example.booking_ma_tim21.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.booking_ma_tim21.R;
import com.google.android.material.button.MaterialButton;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReservationBarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReservationBarFragment extends Fragment {

    public TextView date;
    public TextView guests;
    public TextView price;

    MaterialButton reserve;
    MaterialButton edit;


    public ReservationBarFragment() {
        // Required empty public constructor
    }

    public static ReservationBarFragment newInstance(String date, Integer guests,Double price) {
        ReservationBarFragment fragment = new ReservationBarFragment();

        Bundle args = new Bundle();
        args.putString("date", date);
        args.putInt("guests", guests);
        args.putDouble("price", price);

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
        return inflater.inflate(R.layout.fragment_reservation_bar, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        date=view.findViewById(R.id.date_res_tv);
        guests=view.findViewById(R.id.guests_res_tv);
        price=view.findViewById(R.id.price_res_tv);
        reserve=view.findViewById(R.id.res_btn);
        edit=view.findViewById(R.id.edit_btn);


    }

    public void editDates(Bundle b){

        date.setText(b.getString("dates"));
        guests.setText(b.getString("guests"));

        //TODO add price change

    }

}