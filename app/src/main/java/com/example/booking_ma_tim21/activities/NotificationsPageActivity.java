package com.example.booking_ma_tim21.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.booking_ma_tim21.R;
import com.example.booking_ma_tim21.adapter.NotificationAdapter;
import com.example.booking_ma_tim21.authentication.AuthManager;
import com.example.booking_ma_tim21.dto.NotificationDTO;
import com.example.booking_ma_tim21.dto.NotificationType;
import com.example.booking_ma_tim21.dto.NotificationTypeUpdateRequestDTO;
import com.example.booking_ma_tim21.dto.UserDTO;
import com.example.booking_ma_tim21.retrofit.NotificationsService;
import com.example.booking_ma_tim21.retrofit.RetrofitService;
import com.example.booking_ma_tim21.retrofit.UserService;
import com.example.booking_ma_tim21.util.NavigationSetup;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationsPageActivity extends AppCompatActivity {
    private AuthManager authManager;

    private UserService userService;
    private NotificationsService notificationsService;
    private List<NotificationDTO> notifications = new ArrayList<>();
    List<NotificationType> types = new ArrayList<>();
    private RecyclerView recyclerView;
    private NotificationAdapter notificationAdapter;

    private MaterialButton updateNotificationsButton;

    private CheckBox checkboxReservationRequest;
    private CheckBox checkboxReservationCancellation;
    private CheckBox checkboxOwnerReview;
    private CheckBox checkboxAccommodationReview;
    private CheckBox checkboxReservationRequestResponse;

    private LinearLayout linReservationRequest;
    private LinearLayout linReservationCancellation;
    private LinearLayout linOwnerReview;
    private LinearLayout linAccommodationReview;
    private LinearLayout linReservationRequestResponse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications_page);
        authManager = AuthManager.getInstance(getApplicationContext());
        NavigationSetup.setupNavigation(this, authManager);

        RetrofitService ref=new RetrofitService();
        userService=ref.getRetrofit().create(UserService.class);
        notificationsService=ref.getRetrofit().create(NotificationsService.class);
        setUpCheckBoxes();
        setNotifications();
        setUpUpdateButton();
    }

    private void setUpCheckBoxes() {
        checkboxAccommodationReview = findViewById(R.id.checkboxAccommodationReview);
        checkboxReservationRequest = findViewById(R.id.checkboxReservationRequest);
        checkboxReservationCancellation = findViewById(R.id.checkboxReservationCancellation);
        checkboxOwnerReview = findViewById(R.id.checkboxOwnerReview);
        checkboxReservationRequestResponse = findViewById(R.id.checkboxReservationRequestResponse);

        linAccommodationReview = findViewById(R.id.linAccommodationReview);
        linReservationRequest = findViewById(R.id.linReservationRequest);
        linReservationCancellation = findViewById(R.id.linReservationCancellation);
        linOwnerReview = findViewById(R.id.linOwnerReview);
        linReservationRequestResponse = findViewById(R.id.linReservationRequestResponse);

        Call call=null;
        call=userService.getUserNotificationTypes(authManager.getUserEmail());

        call.enqueue(new Callback<List<NotificationType>>() {
            @Override
            public void onResponse(Call<List<NotificationType>> call, Response<List<NotificationType>> response) {
                if (response.code() == 200) {
                    Log.d("REZ", "Message recieved");
                    types = response.body();
                    if (types == null) return;
                    checkboxAccommodationReview.setChecked(types.contains(NotificationType.ACCOMMODATION_REVIEW));
                    checkboxReservationRequest.setChecked(types.contains(NotificationType.RESERVATION_REQUEST));
                    checkboxReservationCancellation.setChecked(types.contains(NotificationType.RESERVATION_CANCELLATION));
                    checkboxOwnerReview.setChecked(types.contains(NotificationType.OWNER_REVIEW));
                    checkboxReservationRequestResponse.setChecked(types.contains(NotificationType.RESERVATION_REQUEST_RESPONSE));
                } else {
                    Log.d("REZ", "Message recieved: " + response.code());
                }
            }
            @Override
            public void onFailure(Call<List<NotificationType>> call, Throwable t) {
                t.printStackTrace();
            }
        });

        if(authManager.getUserRole().equals("OWNER")){
            linReservationRequestResponse.setVisibility(View.GONE);
        }else if (authManager.getUserRole().equals("GUEST")){
            linAccommodationReview.setVisibility(View.GONE);
            linReservationRequest.setVisibility(View.GONE);
            linReservationCancellation.setVisibility(View.GONE);
            linOwnerReview.setVisibility(View.GONE);
        }
    }

    private void setUpUpdateButton() {
        updateNotificationsButton = findViewById(R.id.update_notifications_btn);

        updateNotificationsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                types.clear();
                if (checkboxAccommodationReview.isChecked()) types.add(NotificationType.ACCOMMODATION_REVIEW);
                if (checkboxReservationRequest.isChecked()) types.add(NotificationType.RESERVATION_REQUEST);
                if (checkboxReservationCancellation.isChecked()) types.add(NotificationType.RESERVATION_CANCELLATION);
                if (checkboxOwnerReview.isChecked()) types.add(NotificationType.OWNER_REVIEW);
                if (checkboxReservationRequestResponse.isChecked()) types.add(NotificationType.RESERVATION_REQUEST_RESPONSE);

                NotificationTypeUpdateRequestDTO requestDTO = new NotificationTypeUpdateRequestDTO(authManager.getUserIdLong(), types);

                Call call=null;
                call=userService.updateUserNotificationTypes(requestDTO);

                call.enqueue(new Callback<UserDTO>() {
                    @Override
                    public void onResponse(Call<UserDTO> call, Response<UserDTO> response) {
                        if (response.code() == 200) {
                            Toast.makeText(getApplicationContext(), "Updated Notifications.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Failed to Update Notifications.", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<UserDTO> call, Throwable t) {
                        t.printStackTrace();
                    }
                });

            }
        });

    }

    private void setNotifications() {
        Call call=null;
        call=notificationsService.getUserNotifications(authManager.getUserIdLong());

        call.enqueue(new Callback<List<NotificationDTO>>() {
            @Override
            public void onResponse(Call<List<NotificationDTO>> call, Response<List<NotificationDTO>> response) {
                if (response.code() == 200) {

                    Log.d("REZ", "Message recieved");

                    notifications=response.body();
                    setRecyclerView(notifications);

                } else {
                    Log.d("REZ", "Message recieved: " + response.code());
                }
            }
            @Override
            public void onFailure(Call<List<NotificationDTO>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    void setRecyclerView(List<NotificationDTO> notifications){
        recyclerView = findViewById(R.id.notifications_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        notificationAdapter = new NotificationAdapter(this, notifications, notificationsService);
        recyclerView.setAdapter(notificationAdapter);

    }

    @Override
    protected void onPause() {
        super.onPause();
        NavigationSetup.closeDrawer(findViewById(R.id.drawerLayout));
    }
}