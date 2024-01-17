package com.example.booking_ma_tim21.adapter.owner_review;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.booking_ma_tim21.R;

public class OwnerReviewViewHolder extends RecyclerView.ViewHolder{
    public TextView textName;
    public TextView textDate;
    public TextView textComment;
    public Button btnDeleteReview;
    public Button btnReportReview;

    public String email;

    public OwnerReviewViewHolder(@NonNull View itemView) {
        super(itemView);

        textName = itemView.findViewById(R.id.textName);
        textDate = itemView.findViewById(R.id.textDate);
        textComment = itemView.findViewById(R.id.textComment);
        btnDeleteReview = itemView.findViewById(R.id.btnDeleteReview);
        btnReportReview = itemView.findViewById(R.id.btnReportReview);
    }

    public String getEmail(){
        return email;
    }
}
