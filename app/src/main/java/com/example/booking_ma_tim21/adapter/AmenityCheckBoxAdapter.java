package com.example.booking_ma_tim21.adapter;

import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.booking_ma_tim21.R;
import com.example.booking_ma_tim21.model.enumeration.Amenity;

import java.util.ArrayList;
import java.util.List;

public class AmenityCheckBoxAdapter extends RecyclerView.Adapter<AmenityCheckBoxAdapter.ViewHolder> {

    private List<Amenity> amenitiesList;
    private SparseBooleanArray checkedItems;

    public AmenityCheckBoxAdapter(List<Amenity> amenitiesList) {
        this.amenitiesList = amenitiesList;
        this.checkedItems = new SparseBooleanArray();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.amenitie_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Amenity amenity = amenitiesList.get(position);
        holder.checkboxAmenity.setText(amenity.name());
        holder.checkboxAmenity.setChecked(checkedItems.get(position, false));

        holder.checkboxAmenity.setOnCheckedChangeListener((buttonView, isChecked) -> {
            checkedItems.put(position, isChecked);
        });
    }

    @Override
    public int getItemCount() {
        return amenitiesList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public CheckBox checkboxAmenity;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            checkboxAmenity = itemView.findViewById(R.id.checkboxAmenity);
        }
    }

    public List<Amenity> getSelectedAmenities() {
        List<Amenity> selectedAmenities = new ArrayList<>();

        for (int i = 0; i < amenitiesList.size(); i++) {
            if (checkedItems.get(i, false)) {
                selectedAmenities.add(amenitiesList.get(i));
            }
        }

        return selectedAmenities;
    }
}