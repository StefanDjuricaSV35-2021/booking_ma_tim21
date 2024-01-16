package com.example.booking_ma_tim21.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.booking_ma_tim21.R;
import com.example.booking_ma_tim21.adapter.ShowPricingsAdapter;
import com.example.booking_ma_tim21.model.AccommodationPricing;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PricingsListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PricingsListFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private List<AccommodationPricing> pricings;

    public PricingsListFragment() {
        // Required empty public constructor
    }

    public static PricingsListFragment newInstance(String param1, String param2) {
        PricingsListFragment fragment = new PricingsListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public void setPricings(List<AccommodationPricing> pricings) {
        this.pricings = pricings;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pricings_list, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setPricingsRecycleView();
    }

    public void setPricingsRecycleView() {
        View rootView = getView();
        if (rootView != null) {
            RecyclerView recyclerView = rootView.findViewById(R.id.pricings_rv);
            recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
            ShowPricingsAdapter adapter = new ShowPricingsAdapter(pricings);
            recyclerView.setAdapter(adapter);
        }
    }
}