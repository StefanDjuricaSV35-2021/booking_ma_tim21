package com.example.booking_ma_tim21.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;

import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.booking_ma_tim21.R;
import com.example.booking_ma_tim21.activities.SearchActivity;
import com.example.booking_ma_tim21.util.DatePickerCreator;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class SearchFragment extends Fragment {

    Button date;
    Button search;
    EditText location;
    EditText guests;

    public SearchFragment() {
        // Required empty public constructor
    }


    public static SearchFragment newInstance() {
        SearchFragment fragment = new SearchFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Transition transition = TransitionInflater.from(requireContext())
                .inflateTransition(R.transition.aaa);
        setSharedElementEnterTransition(transition);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_search_open, container, false);
        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        createPicker();
        setTransitionComponents(view);
        initalizeFields(view);

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MaterialDatePicker picker = createPicker();

                picker.show(getActivity().getSupportFragmentManager(),"TAG");

            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = createIntent();
                startActivity(intent);

            }
        });

    }

    void setTransitionComponents(View view){

        ViewCompat.setTransitionName(view.findViewById(R.id.search_bg_ll), "openBg");
        ViewCompat.setTransitionName(view.findViewById(R.id.search_btn), "openSearch");
    }

    MaterialDatePicker createPicker(){

        MaterialDatePicker<Pair<Long,Long>> picker = DatePickerCreator.getDatePicker(null);

        picker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Pair<Long,Long>>() {
            @Override
            public void onPositiveButtonClick(Pair<Long,Long> selection) {
                Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
                calendar.setTimeInMillis(selection.first);
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                String formattedDate1  = format.format(calendar.getTime());

                calendar.setTimeInMillis(selection.second);
                String formattedDate2  = format.format(calendar.getTime());

                date.setText(formattedDate1+"/"+formattedDate2);
            }
        });

        return picker;

    }

    void initalizeFields(View view){

        date = view.findViewById(R.id.date_range_btn);
        search=view.findViewById(R.id.search_btn);
        location=view.findViewById(R.id.location_et);
        guests=view.findViewById(R.id.guests_et);

    }

    Intent createIntent( ){

        String guests=this.guests.getText().toString();
        String date=this.date.getText().toString();
        String location=this.location.getText().toString();

        Intent intent=new Intent(getActivity(), SearchActivity.class);

        intent.putExtra("guests",guests);
        intent.putExtra("date",date);
        intent.putExtra("location",location);

        return intent;

    }
}