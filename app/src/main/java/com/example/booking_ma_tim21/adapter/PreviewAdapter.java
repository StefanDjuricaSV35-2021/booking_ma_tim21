package com.example.booking_ma_tim21.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.example.booking_ma_tim21.R;
import com.example.booking_ma_tim21.dto.AccommodationPreviewDTO;
import com.squareup.picasso.Picasso;

public class PreviewAdapter extends RecyclerView.Adapter<PreviewViewHolder> {

    Context context;
    List<AccommodationPreviewDTO> accommodationPreviews;
    private ItemClickListener itemListener;

    public PreviewAdapter(Context context, List<AccommodationPreviewDTO> accommodationPreviews, ItemClickListener itemListener) {
        this.context = context;
        this.accommodationPreviews = accommodationPreviews;
        this.itemListener=itemListener;
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


        Picasso.get().load("http://10.0.2.2:8080/images/"+accommodationPreviews.get(position).getImage()).into(holder.image);

        holder.itemView.setOnClickListener(view -> {
            itemListener.onItemClick(accommodationPreviews.get(position));
        });

    }

    public interface ItemClickListener{
        void onItemClick(AccommodationPreviewDTO previews);
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

    public List<AccommodationPreviewDTO> getAccommodationPreviews() {
        return accommodationPreviews;
    }

    public void setAccommodationPreviews(List<AccommodationPreviewDTO> accommodationPreviews) {
        this.accommodationPreviews = accommodationPreviews;
    }
}
