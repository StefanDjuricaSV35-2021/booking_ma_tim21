package com.example.booking_ma_tim21.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.example.booking_ma_tim21.adapter.owner_review.OwnerReviewAdapter;
import com.example.booking_ma_tim21.authentication.AuthManager;
import com.example.booking_ma_tim21.dto.AccommodationDetailsDTO;
import com.example.booking_ma_tim21.dto.AccommodationReviewDTO;
import com.example.booking_ma_tim21.dto.OwnerReviewDTO;
import com.example.booking_ma_tim21.dto.UserDTO;
import com.example.booking_ma_tim21.retrofit.AccommodationReviewService;
import com.example.booking_ma_tim21.retrofit.AccommodationService;
import com.example.booking_ma_tim21.retrofit.OwnerReviewService;
import com.example.booking_ma_tim21.retrofit.RetrofitService;
import com.example.booking_ma_tim21.retrofit.UserService;
import com.example.booking_ma_tim21.util.NavigationSetup;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GuestOwnerReview extends AppCompatActivity {
    private OwnerReviewService ownerReviewService;
    private UserService userService;
    private AuthManager authManager;

    private Long ownerId;

    private String ownerEmail;

    public Long userId;
    private String role;
    private String userEmail;
    private List<OwnerReviewDTO> ownerReviews;
    private double averageGrade;
    private String email = "Email not found";

    private TextView nameTextView;
    private EditText ratingEditText;
    private EditText descriptionEditText;
    private Button submitReviewButton;
    private View starIconsLayout;
    private RecyclerView ownerReviewRecyclerView;

    private OwnerReviewAdapter reviewAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_owner_review);

        authManager = AuthManager.getInstance(getApplicationContext());
        NavigationSetup.setupNavigation(this, authManager);

        RetrofitService retrofitService = new RetrofitService();
        ownerReviewService = retrofitService.getRetrofit().create(OwnerReviewService.class);
        userService = retrofitService.getRetrofit().create(UserService.class);

        nameTextView = findViewById(R.id.nameTextView);
        ratingEditText = findViewById(R.id.ratingEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        submitReviewButton = findViewById(R.id.submitReviewButton);
        ownerReviewRecyclerView = findViewById(R.id.ownerReviewRecyclerView);
        starIconsLayout = findViewById(R.id.starIconsLayout);


        role = authManager.getUserRole();
        if(role != "GUEST"){
            Toast.makeText(this, "You need to be a guest in order to see owner reviews!!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        userEmail = authManager.getUserId();


        getUserByEmail(this.userEmail);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            ownerEmail = extras.getString("OWNER_ID", null);

            if (ownerEmail == null) {
                Toast.makeText(this, "Invalid Owner ID", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
        }
        nameTextView.setText(ownerEmail);

        getOwnerId(this);

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

            OwnerReviewDTO newReview = new OwnerReviewDTO();
            newReview.setId(0l);
            newReview.setReviewerId(this.userId);
            newReview.setReviewedId(this.ownerId);
            newReview.setComment(descriptionText);
            newReview.setRating(rating);
            newReview.setTimePosted(System.currentTimeMillis());

            createOwnerReview(newReview);
        });
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
                        Toast.makeText(GuestOwnerReview.this, "Can't load user!! " + response.code(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(GuestOwnerReview.this, "Response Error! Can't load user!! " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserDTO> call, Throwable t) {
                Toast.makeText(GuestOwnerReview.this, "HTTP Error! Can't load user!! " + t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void getOwnerId(Context context){
        Call<UserDTO> call = userService.getUser(ownerEmail);

        call.enqueue(new Callback<UserDTO>() {
            @Override
            public void onResponse(Call<UserDTO> call, Response<UserDTO> response) {
                if (response.isSuccessful()) {
                    UserDTO userDTO = response.body();
                    if (userDTO != null && userDTO.getEmail() != null) {
                        ownerId = userDTO.getId();
                        getOwnerReviews(context);

                    } else {
                        Toast.makeText(GuestOwnerReview.this, "Can't load owner!! " + response.code(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(GuestOwnerReview.this, "Response Error! Can't load owner!! " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserDTO> call, Throwable t) {
                Toast.makeText(GuestOwnerReview.this, "HTTP Error! Can't load owner!! " + t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    public void getOwnerReviews(Context context){
        Call<List<OwnerReviewDTO>> call = ownerReviewService.getOwnerReviews(ownerId);
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
                    Toast.makeText(GuestOwnerReview.this, "Failed to get owner reviews.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<OwnerReviewDTO>> call, Throwable t) {
                Toast.makeText(GuestOwnerReview.this, "Network error", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void createOwnerReview(OwnerReviewDTO reviewDTO){
        Call<OwnerReviewDTO> call = ownerReviewService.createOwnerReview(reviewDTO);

        call.enqueue(new Callback<OwnerReviewDTO>() {
            @Override
            public void onResponse(Call<OwnerReviewDTO> call, Response<OwnerReviewDTO> response) {
                if (response.isSuccessful()) {
                    OwnerReviewDTO result = response.body();
                    reviewAdapter.add(result);
                    Toast.makeText(GuestOwnerReview.this, "Review added.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(GuestOwnerReview.this, "You are not eligible for a review.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<OwnerReviewDTO> call, Throwable t) {
                Toast.makeText(GuestOwnerReview.this, "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}