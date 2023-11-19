package com.example.booking_ma_tim21.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.example.booking_ma_tim21.R;
import com.example.booking_ma_tim21.model.AccommodationPreview;

public class PreviewAdapter extends RecyclerView.Adapter<PreviewViewHolder> {

    Context context;

    List<AccommodationPreview> accommodationPreviews;

    public PreviewAdapter(Context context, List<AccommodationPreview> accommodationPreviews) {
        this.context = context;
        this.accommodationPreviews = accommodationPreviews;
    }

    @NonNull
    @Override
    public PreviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.cl_accommodation_preview,parent,false);
        return new PreviewViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull PreviewViewHolder holder, int position) {

        holder.name.setText(accommodationPreviews.get(position).getName());
        holder.location.setText(accommodationPreviews.get(position).getLocation());
        holder.image.setImageResource(accommodationPreviews.get(position).getImageUrl());
        holder.price.setText(accommodationPreviews.get(position).getPrice());

    }

    @Override
    public int getItemCount() {
        return accommodationPreviews.size();
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public List<AccommodationPreview> getAccommodationPreviews() {
        return accommodationPreviews;
    }

    public void setAccommodationPreviews(List<AccommodationPreview> accommodationPreviews) {
        this.accommodationPreviews = accommodationPreviews;
    }
}
