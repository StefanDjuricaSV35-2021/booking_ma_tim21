package com.example.booking_ma_tim21.adapter;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.booking_ma_tim21.R;
import com.google.android.material.button.MaterialButton;

public class ReviewReportViewHolder extends RecyclerView.ViewHolder {
    public String email;
    public TextView textName;
    public TextView textDate;
    public TextView textComment;
    public TextView reporterEmail;
    public MaterialButton accept_btn;
    public MaterialButton reject_btn;

    public ReviewReportViewHolder(@NonNull View itemView) {
        super(itemView);

        textName = itemView.findViewById(R.id.text_name);
        textDate = itemView.findViewById(R.id.accommodation_review_text_date);
        textComment = itemView.findViewById(R.id.accommodation_review_comment);
        reporterEmail = itemView.findViewById(R.id.accommodation_reporter_email);
        accept_btn=itemView.findViewById(R.id.accept_accommodation_report_btn);
        reject_btn=itemView.findViewById(R.id.reject_accommodation_report_btn);
    }

}