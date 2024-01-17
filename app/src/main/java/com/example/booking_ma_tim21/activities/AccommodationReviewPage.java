package com.example.booking_ma_tim21.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.booking_ma_tim21.R;
import com.example.booking_ma_tim21.adapter.accommodation_review.AccommodationReviewAdapter;
import com.example.booking_ma_tim21.authentication.AuthManager;
import com.example.booking_ma_tim21.dto.AccommodationDetailsDTO;
import com.example.booking_ma_tim21.dto.AccommodationReviewDTO;
import com.example.booking_ma_tim21.dto.UserDTO;
import com.example.booking_ma_tim21.retrofit.AccommodationReviewService;
import com.example.booking_ma_tim21.retrofit.AccommodationService;
import com.example.booking_ma_tim21.retrofit.RetrofitService;
import com.example.booking_ma_tim21.retrofit.UserService;
import com.example.booking_ma_tim21.util.NavigationSetup;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccommodationReviewPage extends AppCompatActivity {
    private AccommodationReviewService accommodationReviewService;
    private UserService userService;
    private AccommodationService accommodationService;
    private AuthManager authManager;

    private Long accommodationId;
    public Long userId;
    private String role;
    private String userEmail;
    private List<AccommodationReviewDTO> accommodationReviews;
    private double averageGrade;
    private String name = "Email not found";

    private TextView nameTextView;
    private EditText ratingEditText;
    private EditText descriptionEditText;
    private Button submitReviewButton;
    private View starIconsLayout;
    private RecyclerView accommodationReviewsRecyclerView;

    private AccommodationReviewAdapter reviewAdapter;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accommodation_review_page);
        authManager = AuthManager.getInstance(getApplicationContext());
        NavigationSetup.setupNavigation(this, authManager);

        RetrofitService retrofitService = new RetrofitService();
        accommodationReviewService = retrofitService.getRetrofit().create(AccommodationReviewService.class);
        userService = retrofitService.getRetrofit().create(UserService.class);
        accommodationService = retrofitService.getRetrofit().create(AccommodationService.class);

        nameTextView = findViewById(R.id.nameTextView);
        ratingEditText = findViewById(R.id.ratingEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        submitReviewButton = findViewById(R.id.submitReviewButton);
        accommodationReviewsRecyclerView = findViewById(R.id.accommodationReviewsRecyclerView);
        starIconsLayout = findViewById(R.id.starIconsLayout);


        role = authManager.getUserRole();


        if(role != "GUEST"){
            Toast.makeText(this, "You need to be a guest in order to see accommodation reviews!!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        userEmail = authManager.getUserId();


        getUserByEmail(this.userEmail);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            accommodationId = extras.getLong("ACCOMMODATION_ID", -1);

            if (accommodationId == -1) {
                Toast.makeText(this, "Invalid Accommodation ID", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
        }

        getAccommodationName();

        getAccommodationReviews(this);

        submitReviewButton.setOnClickListener(v -> {
            String ratingText = ratingEditText.getText().toString();
            String descriptionText = descriptionEditText.getText().toString();

            int rating = 0;
            try {
                rating = Integer.parseInt(ratingText);
            } catch (NumberFormatException e) {

                rating = -1;
                e.printStackTrace();
            }

            if(rating == -1 || descriptionText.isEmpty()){
                Toast.makeText(this, "Fill out the form to submit the review.", Toast.LENGTH_SHORT).show();
                return;
            }

            AccommodationReviewDTO newReview = new AccommodationReviewDTO();
            newReview.setId(0l);
            newReview.setReviewerId(this.userId);
            newReview.setAccommodationId(this.accommodationId);
            newReview.setComment(descriptionText);
            newReview.setRating(rating);
            newReview.setTimePosted(System.currentTimeMillis());

            createAccommodationReview(newReview);
        });
    }

    public void calculateAverage() {
        double sum = 0;
        for (AccommodationReviewDTO review : accommodationReviews) {
            sum += review.getRating();
        }

        if (sum == 0) {
            averageGrade = 5;
            return;
        }

        averageGrade = roundUpToNearestHalf(sum / accommodationReviews.size());
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

    public void getUserByEmail(String email) {
        Call<UserDTO> call = userService.getUser(email);

        call.enqueue(new Callback<UserDTO>() {
            @Override
            public void onResponse(Call<UserDTO> call, Response<UserDTO> response) {
                if (response.isSuccessful()) {
                    UserDTO userDTO = response.body();
                    if (userDTO != null && userDTO.getEmail() != null) {
                        userId = userDTO.getId();

                    } else {
                        Toast.makeText(AccommodationReviewPage.this, "Can't load user!! " + response.code(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(AccommodationReviewPage.this, "Response Error! Can't load user!! " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserDTO> call, Throwable t) {
                Toast.makeText(AccommodationReviewPage.this, "HTTP Error! Can't load user!! " + t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void getAccommodationName(){
        Call<AccommodationDetailsDTO> call = accommodationService.getAccommodation(accommodationId);
        call.enqueue(new Callback<AccommodationDetailsDTO>() {
            @Override
            public void onResponse(Call<AccommodationDetailsDTO> call, Response<AccommodationDetailsDTO> response) {
                if (response.isSuccessful()) {
                    AccommodationDetailsDTO data = response.body();
                    if (data != null && data.getName() != null) {
                        name = data.getName();
                        nameTextView.setText(name);
                    }
                } else {
                    Toast.makeText(AccommodationReviewPage.this, "Failed to get accommodation details", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AccommodationDetailsDTO> call, Throwable t) {
                Toast.makeText(AccommodationReviewPage.this, "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getAccommodationReviews(Context context){
        Call<List<AccommodationReviewDTO>> call = accommodationReviewService.getAccommodationReviews(accommodationId);
        call.enqueue(new Callback<List<AccommodationReviewDTO>>() {
            @Override
            public void onResponse(Call<List<AccommodationReviewDTO>> call, Response<List<AccommodationReviewDTO>> response) {
                if (response.isSuccessful()) {
                    List<AccommodationReviewDTO> data = response.body();
                    if (data != null) {
                        accommodationReviews = data;
                        for (AccommodationReviewDTO a: accommodationReviews) {
                            Log.d("reviewTest", "Accommodation review id: "+ a.getId());

                        }
                    } else {
                        accommodationReviews = new ArrayList<>();
                    }


                    calculateAverage();
                    String starIcons = getStarIcons(averageGrade);
                    addStarsToLayout(starIcons);

                    reviewAdapter = new AccommodationReviewAdapter(context,accommodationReviews,role,userEmail);
                    accommodationReviewsRecyclerView.setAdapter(reviewAdapter);
                    accommodationReviewsRecyclerView.setLayoutManager(new LinearLayoutManager(context));

                } else {
                    Toast.makeText(AccommodationReviewPage.this, "Failed to get accommodation reviews.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<AccommodationReviewDTO>> call, Throwable t) {
                Toast.makeText(AccommodationReviewPage.this, "Network error", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void createAccommodationReview(AccommodationReviewDTO reviewDTO){
        Call<AccommodationReviewDTO> call = accommodationReviewService.createAccommodationReview(reviewDTO);

        call.enqueue(new Callback<AccommodationReviewDTO>() {
            @Override
            public void onResponse(Call<AccommodationReviewDTO> call, Response<AccommodationReviewDTO> response) {
                if (response.isSuccessful()) {
                    AccommodationReviewDTO result = response.body();
                    reviewAdapter.add(result);
                    Toast.makeText(AccommodationReviewPage.this, "Review added.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AccommodationReviewPage.this, "You are not eligible for a review.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AccommodationReviewDTO> call, Throwable t) {
                Toast.makeText(AccommodationReviewPage.this, "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}