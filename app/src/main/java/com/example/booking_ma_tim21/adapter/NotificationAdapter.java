package com.example.booking_ma_tim21.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.booking_ma_tim21.R;
import com.example.booking_ma_tim21.authentication.AuthManager;
import com.example.booking_ma_tim21.dto.NotificationDTO;
import com.example.booking_ma_tim21.retrofit.NotificationsService;
import com.google.android.material.button.MaterialButton;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {
    Context context;
    List<NotificationDTO> notificationDTOS;
    NotificationsService notificationsService;

    public NotificationAdapter(Context context, List<NotificationDTO> notificationDTOS, NotificationsService notificationsService) {
        this.context = context;
        this.notificationDTOS = notificationDTOS;
        this.notificationsService = notificationsService;
    }
    @NonNull
    @Override
    public NotificationAdapter.NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.cl_notification,parent,false);
        return new NotificationAdapter.NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.NotificationViewHolder holder, int position) {
        holder.type.setText(notificationDTOS.get(position).getType().toString());
        holder.message.setText(notificationDTOS.get(position).getMessage());

        holder.remove_button.setOnClickListener(view -> {

            Call call=notificationsService.deleteNotifications(notificationDTOS.get(position).getId());

            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.code() == 200) {
                        Toast.makeText(context, "Deleted Notification.", Toast.LENGTH_SHORT).show();
                        notificationDTOS.remove(holder.getAdapterPosition());
                        notifyItemRemoved(holder.getAdapterPosition());
                        notifyDataSetChanged();
                    } else {
                        Toast.makeText(context, "Failed to Delete Notification.", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        });
    }

    @Override
    public int getItemCount() {
        return notificationDTOS != null ? notificationDTOS.size() : 0;
    }

    public class NotificationViewHolder extends RecyclerView.ViewHolder {

        TextView type;
        TextView message;
        MaterialButton remove_button;


        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);

            type=itemView.findViewById(R.id.notification_type);
            message=itemView.findViewById(R.id.notification_message);
            remove_button=itemView.findViewById(R.id.notification_remove_btn);
        }

    }
}
