package com.example.booking_ma_tim21.activities;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.booking_ma_tim21.R;
import com.example.booking_ma_tim21.authentication.AuthManager;
import com.example.booking_ma_tim21.model.JWTAuthenticationResponse;
import com.example.booking_ma_tim21.model.SignInRequest;
import com.example.booking_ma_tim21.retrofit.AuthService;
import com.example.booking_ma_tim21.retrofit.RetrofitService;
import com.example.booking_ma_tim21.util.NavigationSetup;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends AppCompatActivity {

    private EditText emailField;
    private EditText passwordField;
    AuthManager authManager;

    AuthService authService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        RetrofitService retrofitService= new RetrofitService();
        authService=retrofitService.getRetrofit().create(AuthService.class);

        authManager = AuthManager.getInstance(getApplicationContext());
        NavigationSetup.setupNavigation(this, authManager);

        emailField = findViewById(R.id.email_field);
        passwordField = findViewById(R.id.password_field);

        Button LoginBtn = findViewById(R.id.button_login);
        LoginBtn.setOnClickListener(v -> {

            String emailValue = emailField.getText().toString();
            String passwordValue = passwordField.getText().toString();

            if (!isValidEmail(emailValue)) {
                Toast.makeText(getApplicationContext(), "Invalid email address", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!isValidPassword(passwordValue)) {
                Toast.makeText(getApplicationContext(), "Invalid password (at least 4 characters required)", Toast.LENGTH_SHORT).show();
                return;
            }

            SignInRequest signInRequest = new SignInRequest(emailValue,passwordValue);

            loginUser(signInRequest);

        });

        Button RegisterBtn = findViewById(R.id.button_register);
        RegisterBtn.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        emailField.setText("");
        passwordField.setText("");
        Log.d("log", "onRestart()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        NavigationSetup.closeDrawer(findViewById(R.id.drawerLayout));
    }

    private boolean isValidEmail(String email) {
        // Use Android's Patterns.EMAIL_ADDRESS for email validation
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isValidPassword(String password) {
        // Example: Password must be at least 4 characters
        return password.length() >= 4;
    }

    private void loginUser(SignInRequest signInRequest) {
        Call<JWTAuthenticationResponse> call = authService.signIn(signInRequest);
        call.enqueue(new Callback<JWTAuthenticationResponse>() {
            @Override
            public void onResponse(Call<JWTAuthenticationResponse> call, Response<JWTAuthenticationResponse> response) {
                if (response.isSuccessful()) {
                    JWTAuthenticationResponse jwtResponse = response.body();

                    authManager.addUser(jwtResponse);
                    Log.d("REZ","userId: "+authManager.getUserId());
                    Log.d("REZ","userRole: "+authManager.getUserRole());

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    Log.d("REZ","Meesage recieved: "+response.code());

                }
            }

            @Override
            public void onFailure(Call<JWTAuthenticationResponse> call, Throwable t) {
                Log.d("REZ","Meesage failed to be received.");
            }
        });
    }
}