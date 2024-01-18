package com.example.booking_ma_tim21.adapter;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
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

    private List<Uri> uriList;
    private Context context;

    public FileAdapter(List<Uri> uriList, Context context) {
        this.uriList = uriList;
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
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.file_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Uri uri = uriList.get(position);
        File file = new File(getPathFromUri(uri));
        holder.fileNameTextView.setText(file.getName());

        holder.removeButton.setOnClickListener(v -> {
            uriList.remove(position);
            notifyItemRemoved(position);
        });
    }

    @Override
    public int getItemCount() {
        return uriList.size();
    }

    public boolean addItem(Uri newItem) {
        for (Uri uri:uriList) {
            if(uri.equals(newItem)){
                return false;
            }
        }
        uriList.add(newItem);
        notifyItemInserted(uriList.size() - 1);
        return true;
    }

    public List<Uri> getFileList() {
        return uriList;
    }

    public List<String> getFileNameList(){
        ArrayList<String> fileNames = new ArrayList<>();
        for (Uri uri:uriList) {
            File file = new File(getPathFromUri(uri));
            fileNames.add(file.getName());
        }
        return fileNames;
    }

    private String getPathFromUri(Uri uri) {
        String path = "";

        if (uri == null) {
            return path;
        }

        try {
            // Check if the URI uses the content scheme
            if ("content".equalsIgnoreCase(uri.getScheme())) {
                // Use ContentResolver to open an InputStream for the URI
                try (Cursor cursor = context.getContentResolver().query(uri, null, null, null, null)) {
                    if (cursor != null && cursor.moveToFirst()) {
                        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                        path = cursor.getString(columnIndex);
                    }
                }
            } else if ("file".equalsIgnoreCase(uri.getScheme())) {
                // For file scheme, directly get the path
                path = uri.getPath();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return path;
    }
}

