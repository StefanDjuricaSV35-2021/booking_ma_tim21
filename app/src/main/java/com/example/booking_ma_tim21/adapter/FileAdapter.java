package com.example.booking_ma_tim21.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.booking_ma_tim21.R;
import com.example.booking_ma_tim21.model.AccommodationPricing;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileAdapter extends RecyclerView.Adapter<FileAdapter.ViewHolder> {

    private List<File> fileList;

    public FileAdapter(List<File> fileList) {
        this.fileList = fileList;
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
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.file_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        File file = fileList.get(position);
        holder.fileNameTextView.setText(file.getName());

        holder.removeButton.setOnClickListener(v -> {
            fileList.remove(position);
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return fileList.size();
    }

    public boolean addItem(File newItem) {
        for (File file:fileList) {
            if(file.getName().equals(newItem.getName())){
                return false;
            }
        }
        fileList.add(newItem);
        notifyItemInserted(fileList.size() - 1);
        return true;
    }

    public List<File> getFileList() {
        return fileList;
    }

    public List<String> getFileNameList(){
        ArrayList<String> fileNames = new ArrayList<>();
        for (File file:fileList) {
            fileNames.add(file.getName());
        }
        return fileNames;
    }
}

