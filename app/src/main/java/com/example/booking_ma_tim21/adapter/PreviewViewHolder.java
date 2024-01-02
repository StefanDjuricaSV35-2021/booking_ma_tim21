package com.example.booking_ma_tim21.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.booking_ma_tim21.R;

public class PreviewViewHolder extends RecyclerView.ViewHolder {

    ImageView image;
    TextView name;
    TextView location;
    TextView price;

    public PreviewViewHolder(@NonNull View itemView) {
        super(itemView);

        image=itemView.findViewById(R.id.preview_image);
        name=itemView.findViewById(R.id.preview_name);
        location=itemView.findViewById(R.id.preview_location);
        price=itemView.findViewById(R.id.preview_price);


    }

}
