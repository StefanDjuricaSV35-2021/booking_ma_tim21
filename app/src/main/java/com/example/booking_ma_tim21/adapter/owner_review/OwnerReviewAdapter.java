package com.example.booking_ma_tim21.adapter.owner_review;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.booking_ma_tim21.R;
import com.example.booking_ma_tim21.activities.OwnerOwnerReview;
import com.example.booking_ma_tim21.dto.OwnerReviewDTO;
import com.example.booking_ma_tim21.dto.ReviewReportDTO;
import com.example.booking_ma_tim21.dto.UserDTO;
import com.example.booking_ma_tim21.retrofit.OwnerReviewService;
import com.example.booking_ma_tim21.retrofit.RetrofitService;
import com.example.booking_ma_tim21.retrofit.ReviewReportService;
import com.example.booking_ma_tim21.retrofit.UserService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OwnerReviewAdapter extends RecyclerView.Adapter<OwnerReviewViewHolder>{
    private Context context;
    private List<OwnerReviewDTO> reviewList;

    private String role;
    private String userEmail;
    private String email;
    private UserService userService;

    private OwnerReviewService ownerReviewService;
    private ReviewReportService reviewReportService;

    public OwnerReviewAdapter(Context context, List<OwnerReviewDTO> reviewList, String role, String userEmail) {
        this.context = context;
        this.reviewList = reviewList;
        RetrofitService retrofitService= new RetrofitService();
        userService=retrofitService.getRetrofit().create(UserService.class);
        ownerReviewService=retrofitService.getRetrofit().create(OwnerReviewService.class);
        reviewReportService=retrofitService.getRetrofit().create(ReviewReportService.class);
        this.role = role;
        this.userEmail = userEmail;
    }

    @NonNull
    @Override
    public OwnerReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.owner_review, parent, false);
        return new OwnerReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OwnerReviewViewHolder holder, int position) {
        OwnerReviewDTO revv = reviewList.get(position);

        getUser(revv.getReviewerId(), holder);
        holder.textDate.setText(formatDate(revv.getTimePosted()) + " " +getStarIcons(revv.getRating()));
        holder.textComment.setText(revv.getComment());

        holder.btnDeleteReview.setOnClickListener(v -> {
            deleteOwnerReview(revv.getId(), position);
        });

        holder.btnReportReview.setOnClickListener(v -> {
            reportOwnerReview(revv.getId(), revv.getReviewedId());
        });
    }

    public void add(OwnerReviewDTO ownerReviewDTO){
        reviewList.add(ownerReviewDTO);
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    private String formatDate(long milliseconds) {
        Date date = new Date(milliseconds);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        return sdf.format(date);
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

    public void setRole(String role) {
        this.role = role;
    }

    public void getUser(Long userId, OwnerReviewViewHolder holder) {
        Call<UserDTO> call = userService.getUser(userId);

        call.enqueue(new Callback<UserDTO>() {
            @Override
            public void onResponse(Call<UserDTO> call, Response<UserDTO> response) {
                if (response.isSuccessful()) {
                    UserDTO userDTO = response.body();
                    if (userDTO != null && userDTO.getEmail() != null) {
                        email = userDTO.getEmail();

                    } else {
                        email = "";
                        System.out.println("Error!");

                    }
                } else {
                    email = "";
                    System.out.println("Response Error!");
                }

                holder.email = email;
                holder.textName.setText(holder.email);

                if(!isCurrentUser(holder.email)){
                    holder.btnDeleteReview.setVisibility(View.GONE);
                    Log.d("rez", "set btnDeleteReview to invisible.");

                }
                if(!isCurrentOwner()){
                    holder.btnReportReview.setVisibility(View.GONE);
                    Log.d("rez", "set btnDeleteReview to invisible.");
                }
            }

            @Override
            public void onFailure(Call<UserDTO> call, Throwable t) {
                email = "";
                System.out.println("HTTP Error!");
            }
        });
    }

    public Boolean isCurrentUser(String email) {
        Log.d("email",email);
        Log.d("email",userEmail);
        if(userEmail.equalsIgnoreCase(email) && "GUEST".equalsIgnoreCase(role)){
            Log.d("email","yes");
        }
        return userEmail.equalsIgnoreCase(email) && "GUEST".equalsIgnoreCase(role);
    }
    public Boolean isCurrentOwner() {
        if("OWNER".equalsIgnoreCase(role)){
            Log.d("email","owner");
        }

        return "OWNER".equalsIgnoreCase(role);
    }

    public void deleteOwnerReview(Long reviewId, int position) {
        Call<Void> call = ownerReviewService.deleteOwnerReview(reviewId);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                reviewList.remove(position);
                notifyItemRemoved(position);

                if (onReviewDeletedListener != null) {
                    onReviewDeletedListener.onReviewDeleted();
                }

                Toast.makeText(context, "Deleted owner review.", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, "Failed to delete owner review.", Toast.LENGTH_SHORT).show();
                Log.d("rez", "failed to delete review.");
            }
        });
    }

    public void reportOwnerReview(Long reviewId, Long reviewedId){
        ReviewReportDTO reportDTO = new ReviewReportDTO(0l,reviewId,reviewedId);

        Call<ReviewReportDTO> call = reviewReportService.createReviewReport(reportDTO);

        call.enqueue(new Callback<ReviewReportDTO>() {
            @Override
            public void onResponse(Call<ReviewReportDTO> call, Response<ReviewReportDTO> response) {
                if (response.isSuccessful()) {
                    ReviewReportDTO result = response.body();
                    Toast.makeText(context, "User review reported.", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(context, "Failed to report user review.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ReviewReportDTO> call, Throwable t) {
                Toast.makeText(context, "Failed to report user review.", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public List<OwnerReviewDTO> getReviewList() {
        return reviewList;
    }

    public void setReviewList(List<OwnerReviewDTO> reviewList) {
        this.reviewList = reviewList;
        notifyDataSetChanged();
    }

    public interface OnReviewDeletedListener {
        void onReviewDeleted();
    }

    private OnReviewDeletedListener onReviewDeletedListener;

    public void setOnReviewDeletedListener(OnReviewDeletedListener listener) {
        this.onReviewDeletedListener = listener;
    }

}
