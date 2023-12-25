package com.example.booking_ma_tim21.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.booking_ma_tim21.R;
import com.example.booking_ma_tim21.authentication.AuthManager;
import com.example.booking_ma_tim21.model.JWTAuthenticationResponse;
import com.example.booking_ma_tim21.model.SignInRequest;
import com.example.booking_ma_tim21.model.SignUpRequest;
import com.example.booking_ma_tim21.model.User;
import com.example.booking_ma_tim21.retrofit.AuthService;
import com.example.booking_ma_tim21.retrofit.RetrofitService;
import com.example.booking_ma_tim21.util.NavigationSetup;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private EditText email_field;
    private EditText password_field;
    private EditText phone_field;
    private EditText street_field;
    private EditText city_field;
    private EditText country_field;
    private EditText name_field;
    private EditText surname_field;
    private EditText repeat_password_field;
    private RadioGroup role_group;
    private RadioButton radio_button_user;
    AuthManager authManager;

    AuthService authService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        RetrofitService retrofitService= new RetrofitService();
        authService=retrofitService.getRetrofit().create(AuthService.class);

        email_field = findViewById(R.id.email_field);
        password_field = findViewById(R.id.password_field);
        phone_field = findViewById(R.id.phone_field);
        street_field = findViewById(R.id.street_field);
        city_field = findViewById(R.id.city_field);
        country_field = findViewById(R.id.country_field);
        name_field = findViewById(R.id.name_field);
        surname_field = findViewById(R.id.surname_field);
        repeat_password_field = findViewById(R.id.repeat_password_field);
        role_group = findViewById(R.id.role_group);
        radio_button_user = findViewById(R.id.radio_button_user);

        authManager = AuthManager.getInstance(getApplicationContext());
        NavigationSetup.setupNavigation(this, authManager);

        Button RegisterBtn = findViewById(R.id.button_register);
        RegisterBtn.setOnClickListener(v -> {

            String email = email_field.getText().toString();
            String password = password_field.getText().toString();
            String phone = phone_field.getText().toString();
            String street = street_field.getText().toString();
            String city = city_field.getText().toString();
            String country = country_field.getText().toString();
            String name = name_field.getText().toString();
            String surname = surname_field.getText().toString();
            String repeat_password = repeat_password_field.getText().toString();

            boolean is_user = radio_button_user.isChecked();

            if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                showToast("Invalid email address");
                return;
            }

            if (TextUtils.isEmpty(password) || password.length() < 4) {
                showToast("Password must be at least 4 characters");
                return;
            }

            if (!password.equals(repeat_password)) {
                showToast("Passwords do not match");
                return;
            }

            if (TextUtils.isEmpty(phone)) {
                showToast("Phone number is required");
                return;
            }

            if (TextUtils.isEmpty(street)) {
                showToast("Street is required");
                return;
            }

            if (TextUtils.isEmpty(city)) {
                showToast("City is required");
                return;
            }

            if (TextUtils.isEmpty(country)) {
                showToast("Country is required");
                return;
            }

            if (TextUtils.isEmpty(name)) {
                showToast("Name is required");
                return;
            }

            if (TextUtils.isEmpty(surname)) {
                showToast("Surname is required");
                return;
            }

            loginUser(new SignUpRequest(email,password,name,surname,country,city,street,phone,!is_user));

        });

        Button LoginBtn = findViewById(R.id.button_login);
        LoginBtn.setOnClickListener(v -> {

            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        email_field.setText("");
        password_field.setText("");
        phone_field.setText("");
        street_field.setText("");
        city_field.setText("");
        country_field.setText("");
        name_field.setText("");
        surname_field.setText("");
        repeat_password_field.setText("");
        radio_button_user.setChecked(true);

        Log.d("log", "onRestart()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        NavigationSetup.closeDrawer(findViewById(R.id.drawerLayout));
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void loginUser(SignUpRequest signUpRequest) {
        Call<User> call = authService.signup(signUpRequest);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    User user = response.body();
                    Log.d("REZ","da.");

                    showToast("Successfully signed up, check your mail for sign up confirmation");

                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    showToast("Failed to sign up");
                    Log.d("REZ","ne.");

                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d("REZ","Meesage failed to be received.");
            }
        });
    }
}