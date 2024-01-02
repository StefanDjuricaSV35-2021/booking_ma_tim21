package com.example.booking_ma_tim21.fragments;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.booking_ma_tim21.R;
import com.example.booking_ma_tim21.model.enumeration.AccommodationType;
import com.example.booking_ma_tim21.model.enumeration.Amenity;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class AccommodationFilterFragment extends DialogFragment {

    public interface OnFilter {
        public void filter(String data);
    }

    private OnFilter dataPassListener;
    private Toolbar toolbar;
    Bundle savedState = null;
    ChipGroup types;
    ChipGroup amenities;
    EditText from;
    EditText to;
    Button filter;


    public AccommodationFilterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            Objects.requireNonNull(dialog.getWindow()).setLayout(width, height);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            dataPassListener = (OnFilter) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnDataPassListener");
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_FRAME, android.R.style.Theme_Holo_Light);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_accommodation_filter, container, false);

    }


    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        setViewComponents();
        fillChipGroups();
        setFormButtonClicks();
        restorePreviousFormState();

    }

    void setViewComponents(){
        View view=getView();
        toolbar = view.findViewById(R.id.toolbar);
        amenities=view.findViewById(R.id.amenities_cg);
        types=view.findViewById(R.id.type_cg);
        from=view.findViewById(R.id.from);
        to=view.findViewById(R.id.to);
        filter=view.findViewById(R.id.btnFilter);

        toolbar.inflateMenu(R.menu.filter);

    }

    void fillChipGroups(){
        fillTypeChipGroup();
        fillAmenityChipGroup();
    }

    void fillAmenityChipGroup(){

        for (Amenity amenity :Amenity.values()) {
            ChipDrawable chipDrawable = ChipDrawable.createFromAttributes(this.requireContext(),
                    null,
                    0,
                    com.google.android.material.R.style.Widget_Material3_Chip_Filter);

            Chip c=new Chip(this.getContext());
            c.setText(amenity.toString());
            c.setChipDrawable(chipDrawable);

            int id=Integer.parseInt("2"+ amenity.ordinal());
            c.setId(id);

            amenities.addView(c);
        }
    }

    void fillTypeChipGroup(){

        for (AccommodationType type :AccommodationType.values()) {
            ChipDrawable chipDrawable = ChipDrawable.createFromAttributes(this.requireContext(),
                    null,
                    0,
                    com.google.android.material.R.style.Widget_Material3_Chip_Filter);

            Chip c=new Chip(this.getContext());
            c.setText(type.toString());
            c.setId(Integer.parseInt("1"+ type.ordinal()));
            c.setChipDrawable(chipDrawable);
            types.addView(c);
        }
    }

    void saveState(){

        Bundle outState=new Bundle();

        ArrayList<Integer> ids = (ArrayList<Integer>) amenities.getCheckedChipIds();
        outState.putIntegerArrayList("Amenities", ids);

        ids= (ArrayList<Integer>) types.getCheckedChipIds();
        outState.putIntegerArrayList("Types", ids);

        outState.putString("From", from.getText().toString());

        outState.putString("To", to.getText().toString());

        savedState =outState;

    }

    void restorePreviousFormState(){

        View view=getView();

        if(savedState ==null){
            return;
        }

        for (String key : savedState.keySet()) {

            switch (key){
                case "Amenities":
                case "Types":

                    List<Integer> ids= (List<Integer>) savedState.get(key);
                    for(int i :ids){
                        Chip c=view.findViewById(i);
                        c.setChecked(true);
                    }
                    break;

                case "From":

                    from.setText(Objects.requireNonNull(savedState.get(key)).toString());
                    break;

                case "To":

                    to.setText(Objects.requireNonNull(savedState.get(key)).toString());
                    break;

                default:
                    break;
            }
        }

    }

    void setFormButtonClicks(){

        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveState();
                String params=constructFilterString();
                passDataToParent(params);
                dismiss();

            }
        });

        toolbar.setNavigationOnClickListener(v -> {

            resetCheckGroups();
            restorePreviousFormState();
            dismiss();

        });

        toolbar.setOnMenuItemClickListener(item -> {

            resetCheckGroups();
            passDataToParent(null);
            dismiss();
            return true;

        });

    }

    void resetCheckGroups(){

        View view=getView();
        List<Integer> ids = amenities.getCheckedChipIds();
        for (Integer id:ids){

            Chip c=view.findViewById(id);
            c.setChecked(false);

        }

        ids=types.getCheckedChipIds();
        for (Integer id:ids){

            Chip c=view.findViewById(id);
            c.setChecked(false);

        }

        from.setText("");
        to.setText("");
    }

    String constructFilterString(){

        StringBuilder params= new StringBuilder();

        List<Integer> ids = amenities.getCheckedChipIds();
        for (Integer id:ids){
            Chip chip = amenities.findViewById(id);

            params.append("Amenity=").append(Amenity.valueOf(chip.getText().toString()).ordinal());

        }

        ids = types.getCheckedChipIds();
        for (Integer id:ids){
            Chip chip = types.findViewById(id);
            params.append("AccommodationType=").append(AccommodationType.valueOf(chip.getText().toString()).ordinal());

        }

        if(!from.getText().toString().isEmpty()){
            params.append("MinPrice=").append(from.getText().toString());

        }

        if(!to.getText().toString().isEmpty()){
            params.append("MaxPrice=").append(to.getText().toString());

        }

        if(params.toString().equals("")){
            return null;
        }
        return params.toString();

    }

    private void passDataToParent(String data) {
        if (dataPassListener != null) {
            dataPassListener.filter(data);
        }
    }
}