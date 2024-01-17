package com.example.booking_ma_tim21.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;

import com.example.booking_ma_tim21.R;
import com.example.booking_ma_tim21.authentication.AuthManager;
import com.example.booking_ma_tim21.dto.UserDTO;
import com.example.booking_ma_tim21.dto.UserReportDTO;
import com.example.booking_ma_tim21.model.UserReportWithEmails;
import com.example.booking_ma_tim21.retrofit.RetrofitService;
import com.example.booking_ma_tim21.retrofit.UserReportService;
import com.example.booking_ma_tim21.retrofit.UserService;
import com.example.booking_ma_tim21.util.NavigationSetup;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserReportsAdminPageActivity extends AppCompatActivity {
    AuthManager authManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_reports_admin_page);
        authManager = AuthManager.getInstance(getApplicationContext());
        NavigationSetup.setupNavigation(this, authManager);

    }

}