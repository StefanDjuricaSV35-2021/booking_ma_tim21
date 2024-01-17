package com.example.booking_ma_tim21.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.booking_ma_tim21.R;
import com.example.booking_ma_tim21.dto.AccommodationChangeRequestDTO;
import com.example.booking_ma_tim21.model.UserReportWithEmails;
import com.squareup.picasso.Picasso;

import java.util.List;

public class UserReportsAdapter extends RecyclerView.Adapter<UserReportsAdapter.UserReportViewHolder>{
    Context context;

    List<UserReportWithEmails> userReports;
    private UserReportsAdapter.ItemClickListener itemListener;

    public UserReportsAdapter(Context context, List<UserReportWithEmails> userReports, UserReportsAdapter.ItemClickListener itemListener) {
        this.context = context;
        this.userReports = userReports;
        this.itemListener=itemListener;
    }
    @NonNull
    @Override
    public UserReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.cl_user_report_preview,parent,false);
        return new UserReportsAdapter.UserReportViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserReportViewHolder holder, int position) {
        holder.reporter_email.setText(userReports.get(position).getReporterUser().getEmail());
        holder.reported_email.setText(userReports.get(position).getReportedUser().getEmail());
        holder.description.setText(userReports.get(position).getDescription());

        holder.itemView.setOnClickListener(view -> {
            itemListener.onItemClick(userReports.get(position));
        });
    }

    public interface ItemClickListener{
        void onItemClick(UserReportWithEmails userReport);
    }

    @Override
    public int getItemCount() {
        return userReports != null ? userReports.size() : 0;
    }


    public class UserReportViewHolder extends RecyclerView.ViewHolder {

        TextView reporter_email;
        TextView description;
        TextView reported_email;

        public UserReportViewHolder(@NonNull View itemView) {
            super(itemView);

            reporter_email=itemView.findViewById(R.id.reporter_user);
            description=itemView.findViewById(R.id.report_description);
            reported_email=itemView.findViewById(R.id.reported_user);
        }

    }
}
