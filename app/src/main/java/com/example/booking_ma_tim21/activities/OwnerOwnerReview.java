package com.example.booking_ma_tim21.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.booking_ma_tim21.R;
import com.example.booking_ma_tim21.adapter.owner_review.OwnerReviewAdapter;
import com.example.booking_ma_tim21.authentication.AuthManager;
import com.example.booking_ma_tim21.dto.OwnerReviewDTO;
import com.example.booking_ma_tim21.dto.UserDTO;
import com.example.booking_ma_tim21.retrofit.OwnerReviewService;
import com.example.booking_ma_tim21.retrofit.RetrofitService;
import com.example.booking_ma_tim21.retrofit.UserService;
import com.example.booking_ma_tim21.util.NavigationSetup;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OwnerOwnerReview extends AppCompatActivity {

    private OwnerReviewService ownerReviewService;
    private UserService userService;
    private AuthManager authManager;

    public Long userId;
    private String role;
    private String userEmail;
    private List<OwnerReviewDTO> ownerReviews;
    private double averageGrade;
    private String email = "Email not found";

    private TextView nameTextView;
    private View starIconsLayout;
    private RecyclerView ownerReviewRecyclerView;
    private OwnerReviewAdapter reviewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_owner_review);

        authManager = AuthManager.getInstance(getApplicationContext());
        NavigationSetup.setupNavigation(this, authManager);

        RetrofitService retrofitService = new RetrofitService();
        ownerReviewService = retrofitService.getRetrofit().create(OwnerReviewService.class);
        userService = retrofitService.getRetrofit().create(UserService.class);

        nameTextView = findViewById(R.id.nameTextView);
        ownerReviewRecyclerView = findViewById(R.id.ownerReviewRecyclerView);
        starIconsLayout = findViewById(R.id.starIconsLayout);


        role = authManager.getUserRole();
        if(role != "OWNER"){
            Toast.makeText(this, "You need to be a owner in order to see your reviews!!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        userEmail = authManager.getUserEmail();

        nameTextView.setText(userEmail);
        getUserByEmail(userEmail, this);
    }

    public void calculateAverage() {
        double sum = 0;
        for (OwnerReviewDTO review : ownerReviews) {
            sum += review.getRating();
        }

        if (sum == 0) {
            averageGrade = 5;
            return;
        }

        averageGrade = roundUpToNearestHalf(sum / ownerReviews.size());
    }

    private double roundUpToNearestHalf(double value) {
        final double maxAllowedValue = 5;
        final double minAllowedValue = 0;
        final double step = 0.5;

        value = Math.max(minAllowedValue, Math.min(maxAllowedValue, value));

        double roundedValue = Math.ceil(value / step) * step;

        return roundedValue;
    }

    private void addStarsToLayout(String starIcons) {
        if (starIconsLayout instanceof LinearLayout) {
            LinearLayout linearLayout = (LinearLayout) starIconsLayout;
            linearLayout.removeAllViews();

            for (int i = 0; i < starIcons.length(); i++) {
                char icon = starIcons.charAt(i);

                ImageView imageView = new ImageView(this);
                if (icon == 'f') {
                    imageView.setImageResource(R.drawable.fullstar);
                } else if (icon == 'h') {
                    imageView.setImageResource(R.drawable.halfstar);
                } else {
                    imageView.setImageResource(R.drawable.star);
                }

                linearLayout.addView(imageView);
            }
        }
    }

    private String getStarIcons(double rating) {
        int fullStars = (int) rating;
        boolean hasHalfStar = rating % 1 != 0;

        StringBuilder stars = new StringBuilder();
        for (int i = 0; i < fullStars; i++) {
            stars.append("f");
        }

        if (hasHalfStar) {
            stars.append("h");
        }

        for (int i = fullStars + (hasHalfStar ? 1 : 0); i < 5; i++) {
            stars.append("e");
        }

        return stars.toString();
    }

    public void getUserByEmail(String email, Context context) {
        Call<UserDTO> call = userService.getUser(email);

        call.enqueue(new Callback<UserDTO>() {
            @Override
            public void onResponse(Call<UserDTO> call, Response<UserDTO> response) {
                if (response.isSuccessful()) {
                    UserDTO userDTO = response.body();
                    if (userDTO != null && userDTO.getEmail() != null) {
                        userId = userDTO.getId();
                        getOwnerReviews(context);

                    } else {
                        Toast.makeText(OwnerOwnerReview.this, "Can't load user!! " + response.code(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(OwnerOwnerReview.this, "Response Error! Can't load user!! " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserDTO> call, Throwable t) {
                Toast.makeText(OwnerOwnerReview.this, "HTTP Error! Can't load user!! " + t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void getOwnerReviews(Context context){
        Call<List<OwnerReviewDTO>> call = ownerReviewService.getOwnerReviews(userId);
        call.enqueue(new Callback<List<OwnerReviewDTO>>() {
            @Override
            public void onResponse(Call<List<OwnerReviewDTO>> call, Response<List<OwnerReviewDTO>> response) {
                if (response.isSuccessful()) {
                    List<OwnerReviewDTO> data = response.body();
                    if (data != null) {
                        ownerReviews = data;
                        for (OwnerReviewDTO a: ownerReviews) {
                            Log.d("reviewTest", "Owner review id: "+ a.getId());
                        }
                    } else {
                        ownerReviews = new ArrayList<>();
                    }


                    calculateAverage();
                    String starIcons = getStarIcons(averageGrade);
                    addStarsToLayout(starIcons);

                    reviewAdapter = new OwnerReviewAdapter(context,ownerReviews,role,userEmail);
                    ownerReviewRecyclerView.setAdapter(reviewAdapter);
                    ownerReviewRecyclerView.setLayoutManager(new LinearLayoutManager(context));

                } else {
                    Toast.makeText(OwnerOwnerReview.this, "Failed to get owner reviews.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<OwnerReviewDTO>> call, Throwable t) {
                Toast.makeText(OwnerOwnerReview.this, "Network error", Toast.LENGTH_SHORT).show();
            }
        });

    }
}