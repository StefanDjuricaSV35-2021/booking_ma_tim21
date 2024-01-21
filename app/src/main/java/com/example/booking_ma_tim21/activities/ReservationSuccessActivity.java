package com.example.booking_ma_tim21.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.booking_ma_tim21.R;
import com.example.booking_ma_tim21.authentication.AuthManager;
import com.example.booking_ma_tim21.util.NavigationSetup;

public class ReservationSuccessActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_success);

        // Display the success message
        TextView successMessageTextView = findViewById(R.id.successMessageTextView);
        successMessageTextView.setText("Reservation Successful!");

        // Button for home navigation
        Button homeButton = findViewById(R.id.homeButton);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToHome();
            }
        });
    }

    private void navigateToHome() {
        Intent intent = new Intent(this, MainActivity.class); // Replace HomeActivity with your home page activity
        startActivity(intent);
        finish(); // Optional: Close this activity from the stack
    }
}