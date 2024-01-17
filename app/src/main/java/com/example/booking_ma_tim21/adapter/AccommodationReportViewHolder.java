package com.example.booking_ma_tim21.adapter;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.booking_ma_tim21.R;
import com.google.android.material.button.MaterialButton;

public class AccommodationReportViewHolder extends RecyclerView.ViewHolder {
    public String email;
    public TextView reviewerEmail;
    public TextView textDate;
    public TextView textComment;
    public TextView reporterEmail;
    public MaterialButton accept_btn;
    public MaterialButton reject_btn;

    public AccommodationReportViewHolder(@NonNull View itemView) {
        super(itemView);

        reviewerEmail = itemView.findViewById(R.id.reviewer_email);
        textDate = itemView.findViewById(R.id.accommodation_review_text_date);
        textComment = itemView.findViewById(R.id.accommodation_review_comment);
        reporterEmail = itemView.findViewById(R.id.accommodation_reporter_email);
        accept_btn=itemView.findViewById(R.id.accept_accommodation_report_btn);
        reject_btn=itemView.findViewById(R.id.reject_accommodation_report_btn);
    }

}