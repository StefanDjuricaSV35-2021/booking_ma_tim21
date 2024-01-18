package com.example.booking_ma_tim21.adapter;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.booking_ma_tim21.R;

import java.util.ArrayList;
import java.util.List;

public class OldPhotosAdapter extends RecyclerView.Adapter<OldPhotosAdapter.ViewHolder> {

    private List<String> photosList = new ArrayList<>();
    private Context context;

    public OldPhotosAdapter(List<String> photosList, Context context) {
        this.photosList = photosList;
        this.context = context;
    }

    // ViewHolder class for your adapter
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView fileNameTextView;
        Button removeButton;

        public ViewHolder(View itemView) {
            super(itemView);
            fileNameTextView = itemView.findViewById(R.id.fileNameTextView);
            removeButton = itemView.findViewById(R.id.removeButton);
        }
    }

    @NonNull
    @Override
    public OldPhotosAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.file_item, parent, false);
        return new OldPhotosAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OldPhotosAdapter.ViewHolder holder, int position) {
        String fileName = photosList.get(position);
        holder.fileNameTextView.setText(fileName);

        holder.removeButton.setOnClickListener(v -> {
            photosList.remove(position);
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return photosList.size();
    }
    public List<String> getPhotosList() {
        return photosList;
    }
}
