package com.example.booking_ma_tim21.adapter.user_report;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.booking_ma_tim21.R;
import com.example.booking_ma_tim21.dto.UserDTO;

public class UserReportViewHolder extends RecyclerView.ViewHolder{
    public TextView textEmail;
    public TextView textName;
    public TextView textLocation;
    public Button btnSelectUser;

    public UserDTO user;

    public UserReportViewHolder(@NonNull View itemView) {
        super(itemView);

        textName = itemView.findViewById(R.id.textName);
        textEmail = itemView.findViewById(R.id.textEmail);
        textLocation = itemView.findViewById(R.id.textLocation);
        btnSelectUser = itemView.findViewById(R.id.btnSelectUser);
    }
    public UserDTO getUser(){
        return user;
    }
}
