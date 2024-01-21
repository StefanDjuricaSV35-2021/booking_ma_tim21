package com.example.booking_ma_tim21.adapter.accommodation_review;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.Visibility;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


import com.example.booking_ma_tim21.R;
import com.example.booking_ma_tim21.activities.AccommodationReviewPage;
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

        getUser(revv.getReviewerId(), holder);
        holder.textDate.setText(formatDate(revv.getTimePosted()) + " " +getStarIcons(revv.getRating()));
        holder.textComment.setText(revv.getComment());

        holder.btnDeleteReview.setOnClickListener(v -> {
            deleteAccommodationReview(revv.getId(), position);
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

    public void getUser(Long userId, AccommodationReviewViewHolder holder) {
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
                Log.d("rez", "set email: "+email);

                if(!isCurrentUser(holder.email)){
                    holder.btnDeleteReview.setVisibility(View.GONE);
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
        return userEmail.equalsIgnoreCase(email) && "GUEST".equalsIgnoreCase(role);
    }

    public void deleteAccommodationReview(Long reviewId, int position) {
        Call<Void> call = accommodationReviewService.deleteAccommodationReview(reviewId);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                reviewList.remove(position);
                notifyItemRemoved(position);
                Log.d("rez", "deleted review: "+reviewId);
                Toast.makeText(context, "Review deleted.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("rez", "failed to delete review.");
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
