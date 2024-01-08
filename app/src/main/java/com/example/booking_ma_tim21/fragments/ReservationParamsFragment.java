package com.example.booking_ma_tim21.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.booking_ma_tim21.R;
import com.example.booking_ma_tim21.model.TimeSlot;
import com.example.booking_ma_tim21.util.DatePickerCreator;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReservationParamsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReservationParamsFragment extends DialogFragment {

    public interface paramsChanged{

        void changedParams(Bundle args);
    }

    private Integer minGuests;
    private Integer maxGuests;
    List<TimeSlot> datesAvailable;

    TextView guests;
    TextView dates;
    MaterialButton confirm;

    paramsChanged pc;

    public ReservationParamsFragment() {
        // Required empty public constructor
    }

    public static ReservationParamsFragment newInstance(Integer minGuests, Integer maxGuests,List<TimeSlot> ts) {
        ReservationParamsFragment fragment = new ReservationParamsFragment();
        Bundle args = new Bundle();
        args.putInt("min", minGuests);
        args.putInt("max", maxGuests);
        args.putParcelableArrayList("dates",(ArrayList)ts);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            minGuests = getArguments().getInt("min");
            maxGuests = getArguments().getInt("max");
            datesAvailable =getArguments().getParcelableArrayList("dates");
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

        dates=view.findViewById(R.id.selected_date_tv);
        guests=view.findViewById(R.id.guests_et);
        confirm=view.findViewById(R.id.confirm_btn);

        setDatesInput();

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReservationBarFragment frag= (ReservationBarFragment) getParentFragment();

                Bundle b=new Bundle();
                b.putString("dates",dates.getText().toString());
                b.putString("guests",guests.getText().toString());

                frag.editDates(b);

            }
        });

    }

    void setDatesInput(){

        dates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MaterialDatePicker mdp= DatePickerCreator.getDatePicker(datesAvailable);

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