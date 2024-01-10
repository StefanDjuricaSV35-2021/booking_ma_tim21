package com.example.booking_ma_tim21.util;


import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.example.booking_ma_tim21.R;
import com.example.booking_ma_tim21.activities.AccommodationCreation;
import com.example.booking_ma_tim21.activities.AccountActivity;
import com.example.booking_ma_tim21.activities.LoginActivity;
import com.example.booking_ma_tim21.activities.MainActivity;
import com.example.booking_ma_tim21.activities.RegisterActivity;
import com.example.booking_ma_tim21.authentication.AuthManager;

public class NavigationSetup {


    public static void setupNavigation(final Activity activity, AuthManager authManager) {

        DrawerLayout drawerLayout = activity.findViewById(R.id.drawerLayout);
        ImageView menu = activity.findViewById(R.id.menu);

        LinearLayout main = activity.findViewById(R.id.mainScreen);
        LinearLayout account = activity.findViewById(R.id.account);
        LinearLayout create_accommodation = activity.findViewById(R.id.create_accommodation);
        LinearLayout your_accommodations = activity.findViewById(R.id.your_accommodations);
        LinearLayout accommodation_creation_requests = activity.findViewById(R.id.accommodation_creation_requests);
        LinearLayout accommodation_updating_requests = activity.findViewById(R.id.accommodation_updating_requests);
        LinearLayout review_reports = activity.findViewById(R.id.review_reports);
        LinearLayout view_reservation_requests = activity.findViewById(R.id.view_reservation_requests);
        LinearLayout view_reservations_guest = activity.findViewById(R.id.view_reservations_guest);
        LinearLayout view_reservations_owner = activity.findViewById(R.id.view_reservations_owner);
        LinearLayout favorite_accommodations = activity.findViewById(R.id.favorite_accommodations);
        LinearLayout owner_reviews = activity.findViewById(R.id.owner_reviews);
        LinearLayout guest_report = activity.findViewById(R.id.guest_report);
        LinearLayout owner_report = activity.findViewById(R.id.owner_report);
        LinearLayout log_out = activity.findViewById(R.id.log_out);
        LinearLayout loginScreen = activity.findViewById(R.id.loginScreen);
        LinearLayout registerScreen = activity.findViewById(R.id.registerScreen);

        String role = authManager.getUserRole() != null ? authManager.getUserRole() : "";

        switch (role) {
            case "GUEST":
                guest_report.setVisibility(View.GONE);
                owner_reviews.setVisibility(View.GONE);
                view_reservations_owner.setVisibility(View.GONE);
                review_reports.setVisibility(View.GONE);
                accommodation_creation_requests.setVisibility(View.GONE);
                accommodation_updating_requests.setVisibility(View.GONE);
                create_accommodation.setVisibility(View.GONE);
                your_accommodations.setVisibility(View.GONE);
                loginScreen.setVisibility(View.GONE);
                registerScreen.setVisibility(View.GONE);
                break;

            case "ADMIN":
                owner_report.setVisibility(View.GONE);
                guest_report.setVisibility(View.GONE);
                owner_reviews.setVisibility(View.GONE);
                favorite_accommodations.setVisibility(View.GONE);
                view_reservations_owner.setVisibility(View.GONE);
                view_reservations_guest.setVisibility(View.GONE);
                view_reservation_requests.setVisibility(View.GONE);
                create_accommodation.setVisibility(View.GONE);
                your_accommodations.setVisibility(View.GONE);
                loginScreen.setVisibility(View.GONE);
                registerScreen.setVisibility(View.GONE);
                break;
            case "OWNER":
                owner_report.setVisibility(View.GONE);
                favorite_accommodations.setVisibility(View.GONE);
                view_reservations_guest.setVisibility(View.GONE);
                review_reports.setVisibility(View.GONE);
                accommodation_creation_requests.setVisibility(View.GONE);
                accommodation_updating_requests.setVisibility(View.GONE);
                loginScreen.setVisibility(View.GONE);
                registerScreen.setVisibility(View.GONE);
                break;
            default:
                owner_report.setVisibility(View.GONE);
                guest_report.setVisibility(View.GONE);
                owner_reviews.setVisibility(View.GONE);
                favorite_accommodations.setVisibility(View.GONE);
                view_reservations_owner.setVisibility(View.GONE);
                view_reservations_guest.setVisibility(View.GONE);
                view_reservation_requests.setVisibility(View.GONE);
                review_reports.setVisibility(View.GONE);
                create_accommodation.setVisibility(View.GONE);
                your_accommodations.setVisibility(View.GONE);
                accommodation_creation_requests.setVisibility(View.GONE);
                accommodation_updating_requests.setVisibility(View.GONE);
                log_out.setVisibility(View.GONE);
                account.setVisibility(View.GONE);
                break;
        }


        menu.setOnClickListener(v -> openDrawer(drawerLayout));

        main.setOnClickListener(v -> redirectActivity(activity, MainActivity.class));
        account.setOnClickListener(v -> redirectActivity(activity, AccountActivity.class));

        create_accommodation.setOnClickListener(v -> redirectActivity(activity, AccommodationCreation.class));
        your_accommodations.setOnClickListener(v -> redirectActivity(activity, MainActivity.class));//promeniti
        accommodation_creation_requests.setOnClickListener(v -> redirectActivity(activity, MainActivity.class));//promeniti
        accommodation_updating_requests.setOnClickListener(v -> redirectActivity(activity, MainActivity.class));//promeniti

        review_reports.setOnClickListener(v -> redirectActivity(activity, MainActivity.class));//promeniti
        view_reservation_requests.setOnClickListener(v -> redirectActivity(activity, MainActivity.class));//promeniti
        view_reservations_guest.setOnClickListener(v -> redirectActivity(activity, MainActivity.class));//promeniti
        view_reservations_owner.setOnClickListener(v -> redirectActivity(activity, MainActivity.class));//promeniti
        favorite_accommodations.setOnClickListener(v -> redirectActivity(activity, MainActivity.class));//promeniti
        owner_reviews.setOnClickListener(v -> redirectActivity(activity, MainActivity.class));//promeniti
        guest_report.setOnClickListener(v -> redirectActivity(activity, MainActivity.class));//promeniti
        owner_report.setOnClickListener(v -> redirectActivity(activity, MainActivity.class));//promeniti

        log_out.setOnClickListener(v -> {
            authManager.signOut();
            redirectActivity(activity, MainActivity.class);
        });

        loginScreen.setOnClickListener(v -> redirectActivity(activity, LoginActivity.class));
        registerScreen.setOnClickListener(v -> redirectActivity(activity, RegisterActivity.class));
    }

    public static void openDrawer(DrawerLayout drawerLayout) {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public static void closeDrawer(DrawerLayout drawerLayout) {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    public static void redirectActivity(Activity activity, Class destination) {
        Intent intent = new Intent(activity, destination);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
        activity.finish();
    }
}