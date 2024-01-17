package com.example.booking_ma_tim21.adapter.user_report;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.booking_ma_tim21.R;
import com.example.booking_ma_tim21.dto.UserDTO;
import com.example.booking_ma_tim21.retrofit.RetrofitService;

import java.util.List;

public class UserReportAdapter  extends RecyclerView.Adapter<UserReportViewHolder>{

    private Context context;
    private List<UserDTO> userList;
    private UserReportViewHolder selectedUser;
    private int selectedUserIndex;
    public UserReportAdapter(Context context, List<UserDTO> userList) {
        this.context = context;
        this.userList = userList;
        selectedUser = null;
        selectedUserIndex = -1;
    }

    @NonNull
    @Override
    public UserReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_report, parent, false);
        return new UserReportViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserReportViewHolder holder, int position) {
        UserDTO user = userList.get(position);
        holder.user = user;
        holder.textEmail.setText(holder.user.getEmail());
        holder.textName.setText(holder.getUser().getName()+" "+holder.getUser().getSurname());
        holder.textLocation.setText(holder.getUser().getStreet()+" , "+holder.getUser().getCity()+" , "+holder.getUser().getCountry());

        holder.btnSelectUser.setOnClickListener(v -> {
            if(selectedUserIndex == holder.getAdapterPosition()) {
                selectedUser.btnSelectUser.setText("Select User");
                selectedUser = null;
                selectedUserIndex = -1;
                Toast.makeText(context, "Deselected user.", Toast.LENGTH_SHORT).show();

            }else{
                if(selectedUserIndex != -1){
                    selectedUser.btnSelectUser.setText("Select User");
                }
                selectedUser = holder;
                holder.btnSelectUser.setText("Currently Selected User");
                selectedUserIndex = holder.getAdapterPosition();
                Toast.makeText(context, "Selected user.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public UserDTO getSelectedUser(){
        if(selectedUser == null){
            return null;
        }
        return selectedUser.getUser();
    }
}
