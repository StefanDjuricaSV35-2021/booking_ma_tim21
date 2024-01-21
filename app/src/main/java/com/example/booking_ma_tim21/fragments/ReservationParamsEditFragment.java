package com.example.booking_ma_tim21.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.booking_ma_tim21.R;
import com.example.booking_ma_tim21.util.DatePickerCreator;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReservationParamsEditFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReservationParamsEditFragment extends DialogFragment {


    Bundle resRestrictions;

    TextView guests;
    TextView dates;
    MaterialButton confirm;

    public ReservationParamsEditFragment() {
        // Required empty public constructor
    }

    public static ReservationParamsEditFragment newInstance(Bundle resRestrictions) {
        ReservationParamsEditFragment fragment = new ReservationParamsEditFragment();

        fragment.setArguments(resRestrictions);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            resRestrictions=getArguments();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reservation_params, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        getDialog().getWindow().setBackgroundDrawableResource(R.drawable.bg_round_corners_white);

        dates=view.findViewById(R.id.selected_date_tv);
        guests=view.findViewById(R.id.guests_et);
        confirm=view.findViewById(R.id.confirm_btn);
        confirm.setVisibility(View.GONE);

        setDatesInput();

        guests.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(s.toString().isEmpty()){
                    guests.setBackgroundResource(R.drawable.bg_round_corners_white_red_borders);
                    confirm.setVisibility(View.GONE);
                    return;
                }

                Integer number=Integer.parseInt(s.toString());
                Integer min=resRestrictions.getInt("min");
                Integer max=resRestrictions.getInt("max");

                if((number>=min&&number<=max)){
                    guests.setBackgroundResource(R.drawable.bg_round_corners_white);
                    confirm.setVisibility(View.VISIBLE);
                }else{
                    guests.setBackgroundResource(R.drawable.bg_round_corners_white_red_borders);
                    confirm.setVisibility(View.GONE);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReservationBarFragment frag= (ReservationBarFragment) getParentFragment();

                Bundle b=new Bundle();
                b.putString("dates",dates.getText().toString());
                b.putString("guests",guests.getText().toString());

                frag.setResParams(b);
                dismiss();

            }
        });

    }

    void setDatesInput(){

        dates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                MaterialDatePicker mdp= DatePickerCreator.getDatePicker(resRestrictions.getParcelableArrayList("dates"));

                mdp.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Pair<Long,Long>>() {
                    @Override
                    public void onPositiveButtonClick(Pair<Long,Long> selection) {
                        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
                        calendar.setTimeInMillis(selection.first);
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                        String formattedDate1  = format.format(calendar.getTime());

                        calendar.setTimeInMillis(selection.second);
                        String formattedDate2  = format.format(calendar.getTime());


                        dates.setText(formattedDate1+"/"+formattedDate2);
                    }
                });

                mdp.show(getActivity().getSupportFragmentManager(),"TAG");

            }
        });

    }

}