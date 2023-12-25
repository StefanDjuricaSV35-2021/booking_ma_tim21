package com.example.booking_ma_tim21.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.booking_ma_tim21.R;
import com.example.booking_ma_tim21.model.enumeration.Amenity;

import java.util.List;

public class AmenitiesAdapter extends RecyclerView.Adapter<AmenitiesAdapter.ViewHolder> {

    private List<Amenity> amenitiesList;

    public AmenitiesAdapter(List<Amenity> amenitiesList) {
        this.amenitiesList = amenitiesList;
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
        holder.textAmenityName.setText(amenity.name());
    }

    @Override
    public int getItemCount() {
        return amenitiesList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkboxAmenity;
        TextView textAmenityName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            checkboxAmenity = itemView.findViewById(R.id.checkboxAmenity);
            textAmenityName = itemView.findViewById(R.id.textAmenityName);
        }
    }
}