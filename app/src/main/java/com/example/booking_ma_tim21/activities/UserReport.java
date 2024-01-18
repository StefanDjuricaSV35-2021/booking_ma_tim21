package com.example.booking_ma_tim21.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.booking_ma_tim21.R;
import com.example.booking_ma_tim21.adapter.user_report.UserReportAdapter;
import com.example.booking_ma_tim21.authentication.AuthManager;
import com.example.booking_ma_tim21.dto.UserDTO;
import com.example.booking_ma_tim21.dto.UserReportDTO;
import com.example.booking_ma_tim21.retrofit.RetrofitService;
import com.example.booking_ma_tim21.retrofit.UserReportService;
import com.example.booking_ma_tim21.retrofit.UserService;
import com.example.booking_ma_tim21.util.NavigationSetup;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserReport extends AppCompatActivity {

    private UserReportService userReportService;
    private UserService userService;
    private AuthManager authManager;

    public Long userId;
    private String role;
    private List<UserDTO> reportableUsers;
    private EditText descriptionEditText;
    private Button submitReportButton;
    private RecyclerView userRecyclerView;

    private UserReportAdapter userReportAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_report);

        authManager = AuthManager.getInstance(getApplicationContext());
        NavigationSetup.setupNavigation(this, authManager);

        RetrofitService retrofitService = new RetrofitService();
        userReportService = retrofitService.getRetrofit().create(UserReportService.class);
        userService = retrofitService.getRetrofit().create(UserService.class);

        descriptionEditText = findViewById(R.id.descriptionEditText);
        submitReportButton = findViewById(R.id.submitReportButton);
        userRecyclerView = findViewById(R.id.userRecyclerView);


        role = authManager.getUserRole();
        if(role== null || (!role.equals("GUEST") && !role.equals("OWNER"))){
            Toast.makeText(this, "You need to be a guest or order in order to report users!!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        userId = authManager.getUserIdLong();

        getUsers(this);

        submitReportButton.setOnClickListener(v -> {
            String descriptionText = descriptionEditText.getText().toString();



            if(descriptionText.isEmpty()){
                Toast.makeText(this, "Fill out the form to submit the report .", Toast.LENGTH_SHORT).show();
                return;
            }

            if(userReportAdapter.getSelectedUser()==null){
                Toast.makeText(this, "Select the user you wish to report to be able to submit the report .", Toast.LENGTH_SHORT).show();
                return;
            }

            UserReportDTO userReportDTO = new UserReportDTO();
            userReportDTO.setReportedId(userReportAdapter.getSelectedUser().getId());
            userReportDTO.setId(0l);
            userReportDTO.setReporterId(userId);
            userReportDTO.setDescription(descriptionText);

            createUserReport(userReportDTO);
        });
    }

    public void getUsers(Context context){
        Call<List<UserDTO>> call;
        if(role.equals("GUEST")){
            call = userReportService.getGuestsOwners(userId);
        }else{
            call = userReportService.getOwnersGuests(userId);
        }
        call.enqueue(new Callback<List<UserDTO>>() {
            @Override
            public void onResponse(Call<List<UserDTO>> call, Response<List<UserDTO>> response) {
                if (response.isSuccessful()) {
                    List<UserDTO> data = response.body();
                    if (data != null) {
                        reportableUsers = data;

                    } else {
                        reportableUsers = new ArrayList<>();
                    }


                    userReportAdapter = new UserReportAdapter(context,reportableUsers);
                    userRecyclerView.setAdapter(userReportAdapter);
                    userRecyclerView.setLayoutManager(new LinearLayoutManager(context));

                } else {
                    Toast.makeText(UserReport.this, "Failed to get users.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<UserDTO>> call, Throwable t) {
                Toast.makeText(UserReport.this, "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void createUserReport(UserReportDTO userReportDTO){
        Call<UserReportDTO> call = userReportService.createUserReport(userReportDTO);

        call.enqueue(new Callback<UserReportDTO>() {
            @Override
            public void onResponse(Call<UserReportDTO> call, Response<UserReportDTO> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(UserReport.this, "Report created.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(UserReport.this, "Failed to create report.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserReportDTO> call, Throwable t) {
                Toast.makeText(UserReport.this, "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}