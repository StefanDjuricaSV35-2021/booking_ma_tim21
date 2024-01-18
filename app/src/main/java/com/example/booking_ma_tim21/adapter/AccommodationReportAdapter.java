package com.example.booking_ma_tim21.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.booking_ma_tim21.R;
import com.example.booking_ma_tim21.dto.AccommodationReviewDTO;
import com.example.booking_ma_tim21.dto.ReviewReportDTO;
import com.example.booking_ma_tim21.dto.UserDTO;
import com.example.booking_ma_tim21.retrofit.AccommodationReviewService;
import com.example.booking_ma_tim21.retrofit.RetrofitService;
import com.example.booking_ma_tim21.retrofit.ReviewReportService;
import com.example.booking_ma_tim21.retrofit.UserService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccommodationReportAdapter extends RecyclerView.Adapter<ReviewReportViewHolder> {
    private Context context;
    private List<ReviewReportDTO> reviewReports;
    private String reporterEmail = "";
    private Long reviewerId = 0L;
    private String reviewerEmail = "";
    private String comment = "";
    private int stars = 0;
    private UserService userService;
    private AccommodationReviewService accommodationReviewService;
    private ReviewReportService reviewReportService;

    public AccommodationReportAdapter(Context context, List<ReviewReportDTO> reviewReports) {
        this.context = context;
        this.reviewReports = reviewReports;
        RetrofitService retrofitService= new RetrofitService();
        userService=retrofitService.getRetrofit().create(UserService.class);
        accommodationReviewService=retrofitService.getRetrofit().create(AccommodationReviewService.class);
        reviewReportService=retrofitService.getRetrofit().create(ReviewReportService.class);

    }

    @NonNull
    @Override
    public ReviewReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cl_accommodation_report_preview, parent, false); // change accommodation_review
        return new ReviewReportViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewReportViewHolder holder, int position) {
        ReviewReportDTO reviewReportDTO = reviewReports.get(position);

        getReporter(reviewReportDTO.getReporterId(), holder);
        getReview(reviewReportDTO.getReportedReviewId(), holder);
        holder.reject_btn.setOnClickListener(v -> {
            rejectRequest(reviewReportDTO.getId(), position);
        });
        holder.accept_btn.setOnClickListener(v -> {
            acceptRequest(reviewReportDTO, position);
        });
    }
    @Override
    public int getItemCount() {
        return reviewReports != null ? reviewReports.size() : 0;
    }


    public void rejectRequest(Long reportId, int position) {
        Call<Void> call = reviewReportService.deleteReviewReport(reportId);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(!reviewReports.isEmpty()) {
                    reviewReports.remove(position);
                    notifyDataSetChanged();
                    notifyItemRemoved(position);
                }
                Toast.makeText(context, "Report Rejected.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("rez", "failed to delete request.");
            }
        });
    }

    public void acceptRequest(ReviewReportDTO reviewReportDTO, int position) {
        Call<Void> newCall = accommodationReviewService.deleteAccommodationReview(reviewReportDTO.getReportedReviewId());
        newCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.d("rez", "Deleted review.");
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("rez", "failed to delete review.");
            }
        });


        Call<Void> call = reviewReportService.deleteReviewReport(reviewReportDTO.getId());
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(!reviewReports.isEmpty()) {
                    reviewReports.remove(position);
                    notifyDataSetChanged();
                    notifyItemRemoved(position);
                }
                Toast.makeText(context, "Report Accepted.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("rez", "failed to delete review.");
            }
        });
    }

    public static String getStarIcons(int rating) {
        StringBuilder stars = new StringBuilder();

        for (int i = 0; i < rating; i++) {
            stars.append("★");
        }

        for (int i = rating; i < 5; i++) {
            stars.append("☆");
        }

        return stars.toString();
    }

    public void getReview(Long reviewId, ReviewReportViewHolder holder) {
        Call<AccommodationReviewDTO> call = accommodationReviewService.getAccommodationReview(reviewId);
        call.enqueue(new Callback<AccommodationReviewDTO>() {
            @Override
            public void onResponse(Call<AccommodationReviewDTO> call, Response<AccommodationReviewDTO> response) {
                if (response.isSuccessful()) {
                    AccommodationReviewDTO reviewDTO = response.body();
                    if (reviewDTO != null) {
                        reviewerId = reviewDTO.getReviewerId();
                        comment = reviewDTO.getComment();
                        stars = reviewDTO.getRating();
                    }
                }
                holder.textComment.setText(comment);
                holder.textDate.setText(getStarIcons(stars));
                getReviewer(reviewerId, holder);
            }
            @Override
            public void onFailure(Call<AccommodationReviewDTO> call, Throwable t) {
                System.out.println("HTTP Error!");
            }
        });
    }

    public void getReporter(Long userId, ReviewReportViewHolder holder) {
        Call<UserDTO> call = userService.getUser(userId);
        call.enqueue(new Callback<UserDTO>() {
            @Override
            public void onResponse(Call<UserDTO> call, Response<UserDTO> response) {
                if (response.isSuccessful()) {
                    UserDTO userDTO = response.body();
                    if (userDTO != null && userDTO.getEmail() != null) {
                        reporterEmail = userDTO.getEmail();

                    }
                }

                holder.reporterEmail.setText(reporterEmail);
                Log.d("rez", "set email: "+reporterEmail);
            }

            @Override
            public void onFailure(Call<UserDTO> call, Throwable t) {
                reporterEmail = "";
                System.out.println("HTTP Error!");
            }
        });
    }

    public void getReviewer(Long userId, ReviewReportViewHolder holder) {
        Call<UserDTO> call = userService.getUser(userId);
        call.enqueue(new Callback<UserDTO>() {
            @Override
            public void onResponse(Call<UserDTO> call, Response<UserDTO> response) {
                if (response.isSuccessful()) {
                    UserDTO userDTO = response.body();
                    if (userDTO != null && userDTO.getEmail() != null) {
                        reviewerEmail = userDTO.getEmail();

                    }
                }

                holder.textName.setText(reviewerEmail);
                Log.d("rez", "set email: "+reviewerEmail);
            }

            @Override
            public void onFailure(Call<UserDTO> call, Throwable t) {
            }
        });
    }
}
