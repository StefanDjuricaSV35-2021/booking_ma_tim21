package com.example.booking_ma_tim21.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.booking_ma_tim21.R;
import com.example.booking_ma_tim21.model.enumeration.Amenity;

import java.util.List;

public class AmenityListAdapter extends RecyclerView.Adapter<AmenityListAdapter.StringViewHolder> {

    private List<Amenity> amenityList;

    public AmenityListAdapter(List<Amenity> amenityList) {
        this.amenityList = amenityList;
    }

    @NonNull
    @Override
    public StringViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tv, parent, false);
        return new StringViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StringViewHolder holder, int position) {
        Amenity currentItem = amenityList.get(position);
        holder.textViewItem.setText(currentItem.toString());
    }

    @Override
    public int getItemCount() {
        return amenityList.size();
    }

    public static class StringViewHolder extends RecyclerView.ViewHolder {
        TextView textViewItem;

        public StringViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewItem = itemView.findViewById(R.id.textViewItem);
        }
    }
}