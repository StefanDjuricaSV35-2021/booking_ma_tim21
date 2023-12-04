package com.example.booking_ma_tim21.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.booking_ma_tim21.R;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

public class SearchClosedFragment extends Fragment {

    Button expandSearch;
    String location;
    String guests;
    String date;

    public interface SearchOpenListener
    {
        public void openSearch();
    }

    public static SearchClosedFragment newInstance(String location, String guests,String date) {
        SearchClosedFragment fragment = new SearchClosedFragment();
        Bundle args = new Bundle();
        args.putString("location", location);
        args.putString("guests", guests);
        args.putString("date", date);

        fragment.setArguments(args);
        return fragment;
    }

    public SearchClosedFragment() {
        // Required empty public constructor
    }


    public static SearchClosedFragment newInstance() {
        SearchClosedFragment fragment = new SearchClosedFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_search_closed, container, false);
        Bundle args = getArguments();
        this.location = args.getString("location", "Location");
        this.guests = args.getString("guests", "Guests");
        this.date = args.getString("date", "Date");

        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        setTransitionComponents(view);

        expandSearch= view.findViewById(R.id.search_btn);

        expandSearch.setText(location+" • "+guests+" • "+date);
        expandSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SearchOpenListener sol= (SearchOpenListener)getActivity();
                sol.openSearch();

            }
        });

    }

    public void setTransitionComponents(View view){
        ViewCompat.setTransitionName(view.findViewById(R.id.search_bg_ll), "closeBg");
        ViewCompat.setTransitionName(view.findViewById(R.id.search_btn), "closeSearch");

    }


}