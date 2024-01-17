package com.example.booking_ma_tim21.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.booking_ma_tim21.R;
import com.example.booking_ma_tim21.dto.AccommodationChangeRequestDTO;
import com.squareup.picasso.Picasso;

import java.util.List;

public class UpdatingPreviewAdapter extends RecyclerView.Adapter<UpdatingPreviewAdapter.UpdatingPreviewViewHolder> {
    Context context;
    List<AccommodationChangeRequestDTO> changeRequestDTOS;
    private UpdatingPreviewAdapter.ItemClickListener itemListener;

    public UpdatingPreviewAdapter(Context context, List<AccommodationChangeRequestDTO> changeRequestDTOS, UpdatingPreviewAdapter.ItemClickListener itemListener) {
        this.context = context;
        this.changeRequestDTOS = changeRequestDTOS;
        this.itemListener=itemListener;
    }
    @NonNull
    @Override
    public UpdatingPreviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.cl_accommodation_uptading_preview,parent,false);
        return new UpdatingPreviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UpdatingPreviewViewHolder holder, int position) {
        holder.name.setText(changeRequestDTOS.get(position).getName());
        holder.location.setText(changeRequestDTOS.get(position).getLocation());
        Picasso.get().load("http://10.0.2.2:8080/images/"+changeRequestDTOS.get(position).getPhotos().get(0)).into(holder.image);

        holder.itemView.setOnClickListener(view -> {
            itemListener.onItemClick(changeRequestDTOS.get(position));
        });
    }
    public interface ItemClickListener{
        void onItemClick(AccommodationChangeRequestDTO changeRequestDTO);
    }
    @Override
    public int getItemCount() {
        return changeRequestDTOS != null ? changeRequestDTOS.size() : 0;
    }



    public class UpdatingPreviewViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView name;
        TextView location;

        public UpdatingPreviewViewHolder(@NonNull View itemView) {
            super(itemView);

            image=itemView.findViewById(R.id.preview_image_updating);
            name=itemView.findViewById(R.id.preview_name_updating);
            location=itemView.findViewById(R.id.preview_location_updating);
        }

    }
}
