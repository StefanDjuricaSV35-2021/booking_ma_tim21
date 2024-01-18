package com.example.booking_ma_tim21.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.booking_ma_tim21.R;
import com.example.booking_ma_tim21.dto.AccommodationChangeRequestDTO;
import com.example.booking_ma_tim21.dto.ReservationDTO;
import com.example.booking_ma_tim21.dto.ReservationRequestDTO;
import com.example.booking_ma_tim21.dto.UserDTO;
import com.example.booking_ma_tim21.model.UserReportWithEmails;
import com.example.booking_ma_tim21.model.enumeration.ReservationRequestStatus;
import com.example.booking_ma_tim21.model.enumeration.ReservationStatus;
import com.example.booking_ma_tim21.retrofit.AccommodationService;
import com.example.booking_ma_tim21.retrofit.ReservationRequestService;
import com.example.booking_ma_tim21.retrofit.ReservationService;
import com.example.booking_ma_tim21.retrofit.RetrofitService;
import com.example.booking_ma_tim21.retrofit.UserReportService;
import com.example.booking_ma_tim21.retrofit.UserService;
import com.google.android.material.button.MaterialButton;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        UserReportWithEmails userReport = userReports.get(position);
        holder.bind(userReport);
    }

    public interface ItemClickListener{
        void onItemClick(UserReportWithEmails userReport);
    }

    @Override
    public int getItemCount() {
        return userReports != null ? userReports.size() : 0;
    }

    public void updateDataset() {
        notifyDataSetChanged();
    }


    public class UserReportViewHolder extends RecyclerView.ViewHolder {

        private UserReportService userReportService;
        private UserService userService;
        private ReservationRequestService reservationRequestService;
        private ReservationService reservationService;
        TextView reporter_email;
        TextView description;
        TextView reported_email;
        MaterialButton accept_btn;
        MaterialButton reject_btn;

        public UserReportViewHolder(@NonNull View itemView) {
            super(itemView);

            RetrofitService ref=new RetrofitService();
            this.userReportService=ref.getRetrofit().create(UserReportService.class);
            this.userService=ref.getRetrofit().create(UserService.class);
            this.reservationService=ref.getRetrofit().create(ReservationService.class);
            this.reservationRequestService=ref.getRetrofit().create(ReservationRequestService.class);

            reporter_email=itemView.findViewById(R.id.reporter_user);
            description=itemView.findViewById(R.id.report_description);
            reported_email=itemView.findViewById(R.id.reported_user);
            accept_btn=itemView.findViewById(R.id.accept_user_report_btn);
            reject_btn=itemView.findViewById(R.id.reject_user_report_btn);
        }

        public void bind(UserReportWithEmails userReport) {
            reporter_email.setText(userReport.getReporterUser().getEmail());
            reported_email.setText(userReport.getReportedUser().getEmail());
            description.setText(userReport.getDescription());

            accept_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    userReport.getReportedUser().setBlocked(true);
                    Call<UserDTO> callUser = userService.updateUser(userReport.getReportedUser());

                    callUser.enqueue(new Callback<UserDTO>() {
                        @Override
                        public void onResponse(Call<UserDTO> call, Response<UserDTO> response) {
                            if (response.isSuccessful()) {

                            } else {
                                Log.e("API Call", "Error: " + response.code());
                            }
                        }
                        @Override
                        public void onFailure(Call<UserDTO> call, Throwable t) {
                            t.printStackTrace();
                        }
                    });

                    reservationService.getCurrentReservations(userReport.getReportedUser().getId()).enqueue(new Callback<List<ReservationDTO>>() {
                        @Override
                        public void onResponse(Call<List<ReservationDTO>> call, Response<List<ReservationDTO>> response) {
                            if (response.isSuccessful()) {
                                List<ReservationDTO> reservations = response.body();
                                for (ReservationDTO reservation : reservations) {
                                    if (reservation.getStatus() == ReservationStatus.Active) {
                                        reservation.setStatus(ReservationStatus.Cancelled);
                                        reservationService.updateReservation(reservation).enqueue(new Callback<ReservationDTO>() {
                                            @Override
                                            public void onResponse(Call<ReservationDTO> call, Response<ReservationDTO> response) {
                                                if (!response.isSuccessful()) {
                                                    Log.e("API Call", "Error updating reservation: " + response.code());
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<ReservationDTO> call, Throwable t) {
                                                t.printStackTrace();
                                            }
                                        });
                                    }
                                }
                            } else {
                                Log.e("API Call", "Error: " + response.code());
                            }
                        }

                        @Override
                        public void onFailure(Call<List<ReservationDTO>> call, Throwable t) {
                            t.printStackTrace();
                        }
                    });

                    reservationRequestService.getUserReservationRequests(userReport.getReportedUser().getId()).enqueue(new Callback<List<ReservationRequestDTO>>() {
                        @Override
                        public void onResponse(Call<List<ReservationRequestDTO>> call, Response<List<ReservationRequestDTO>> response) {
                            if (response.isSuccessful()) {
                                List<ReservationRequestDTO> reservationRequests = response.body();
                                for (ReservationRequestDTO request : reservationRequests) {
                                    if (request.getStatus() == ReservationRequestStatus.Accepted || request.getStatus() == ReservationRequestStatus.Waiting) {
                                        request.setStatus(ReservationRequestStatus.Cancelled);
                                        reservationRequestService.updateReservationRequest(request).enqueue(new Callback<ReservationRequestDTO>() {
                                            @Override
                                            public void onResponse(Call<ReservationRequestDTO> call, Response<ReservationRequestDTO> response) {
                                                if (!response.isSuccessful()) {
                                                    Log.e("API Call", "Error updating reservation request: " + response.code());
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<ReservationRequestDTO> call, Throwable t) {
                                                t.printStackTrace();
                                            }
                                        });
                                    }
                                }
                            } else {
                                Log.e("API Call", "Error: " + response.code());
                            }
                        }

                        @Override
                        public void onFailure(Call<List<ReservationRequestDTO>> call, Throwable t) {
                            t.printStackTrace();
                        }
                    });

                    Call<Void> call = userReportService.deleteReport(userReport.getId());

                    call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.isSuccessful()) {
                                userReports.remove(userReport);
                                updateDataset();
                                Toast.makeText(context, "Report Accepted.", Toast.LENGTH_SHORT).show();
                            } else {
                                Log.e("API Call", "Error: " + response.code());
                            }
                        }
                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            t.printStackTrace();
                        }
                    });
                }
            });
            reject_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Call<Void> call = userReportService.deleteReport(userReport.getId());

                    call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.isSuccessful()) {
                                userReports.remove(userReport);
                                updateDataset();
                                Toast.makeText(context, "Report Rejected.", Toast.LENGTH_SHORT).show();
                            } else {
                                Log.e("API Call", "Error: " + response.code());
                            }
                        }
                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            t.printStackTrace();
                        }
                    });
                }
            });
        }

    }
}
