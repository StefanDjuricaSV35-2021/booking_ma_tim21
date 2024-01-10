package com.example.booking_ma_tim21.adapter.accommodation_review;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.Visibility;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CountDownLatch;


import com.example.booking_ma_tim21.R;
import com.example.booking_ma_tim21.dto.AccommodationReviewDTO;
import com.example.booking_ma_tim21.dto.UserDTO;
import com.example.booking_ma_tim21.retrofit.AccommodationReviewService;
import com.example.booking_ma_tim21.retrofit.RetrofitService;
import com.example.booking_ma_tim21.retrofit.UserService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccommodationReviewAdapter extends RecyclerView.Adapter<AccommodationReviewViewHolder> {
    private Context context;
    private List<AccommodationReviewDTO> reviewList;

    private String role;
    private String userEmail;
    private String email;
    private UserService userService;

    private AccommodationReviewService accommodationReviewService;

    public AccommodationReviewAdapter(Context context, List<AccommodationReviewDTO> reviewList, String role, String userEmail) {
        this.context = context;
        this.reviewList = reviewList;
        RetrofitService retrofitService= new RetrofitService();
        userService=retrofitService.getRetrofit().create(UserService.class);
        accommodationReviewService=retrofitService.getRetrofit().create(AccommodationReviewService.class);
        this.role = role;
        this.userEmail = userEmail;
    }

    @NonNull
    @Override
    public AccommodationReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.accommodation_review, parent, false);
        return new AccommodationReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AccommodationReviewViewHolder holder, int position) {
        AccommodationReviewDTO revv = reviewList.get(position);

        getUser(revv.getReviewerId());
        holder.email = email;
        holder.textName.setText(holder.email);


        holder.textDate.setText(formatDate(revv.getTimePosted()) + " " +getStarIcons(revv.getRating()));
        holder.textComment.setText(revv.getComment());

        if(!isCurrentUser(holder.email)){
            holder.btnDeleteReview.setVisibility(View.GONE);
        }

        holder.btnDeleteReview.setOnClickListener(v -> {
            deleteAccommodationReview(revv.getId(), new DeleteReviewCallback() {
                @Override
                public void onDeleteSuccess() {
                    int position = reviewList.indexOf(revv);
                    if (position != -1) {
                        reviewList.remove(position);
                        notifyItemRemoved(position);
                    }
                }

                @Override
                public void onDeleteFailure(String errorMessage) {
                    System.out.println(errorMessage);
                }
            });
        });
    }

    public void add(AccommodationReviewDTO accommodationReviewDTO){
        reviewList.add(accommodationReviewDTO);
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

    public void getUser(Long userId) {
        final CountDownLatch latch = new CountDownLatch(1);
        Call<UserDTO> call = userService.getUser(userId);

        call.enqueue(new Callback<UserDTO>() {
            @Override
            public void onResponse(Call<UserDTO> call, Response<UserDTO> response) {
                if (response.isSuccessful()) {
                    UserDTO userDTO = response.body();
                    if (userDTO != null && userDTO.getEmail() != null) {
                        email = userDTO.getEmail();
                        latch.countDown();
                    } else {
                        email = "";
                        System.out.println("Error!");
                        latch.countDown();
                    }
                } else {
                    email = "";
                    System.out.println("Response Error!");
                    latch.countDown();
                }
            }

            @Override
            public void onFailure(Call<UserDTO> call, Throwable t) {
                email = "";
                System.out.println("HTTP Error!");
                latch.countDown();
            }
        });

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public Boolean isCurrentUser(String email) {
        return userEmail.equalsIgnoreCase(email) && "GUEST".equalsIgnoreCase(role);
    }

    public interface DeleteReviewCallback {
        void onDeleteSuccess();

        void onDeleteFailure(String errorMessage);
    }

    public void deleteAccommodationReview(Long reviewId, DeleteReviewCallback callback) {
        Call<Void> call = accommodationReviewService.deleteAccommodationReview(reviewId);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // Handle successful deletion
                    callback.onDeleteSuccess();
                } else {
                    // Handle error response
                    callback.onDeleteFailure("Failed to delete review. HTTP status code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Handle network or unexpected errors
                callback.onDeleteFailure("Failed to delete review. Error: " + t.getMessage());
            }
        });
    }

    public List<AccommodationReviewDTO> getReviewList() {
        return reviewList;
    }

    public void setReviewList(List<AccommodationReviewDTO> reviewList) {
        this.reviewList = reviewList;
        notifyDataSetChanged();
    }
}
