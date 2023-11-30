package com.example.booking_ma_tim21.util;


import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.example.booking_ma_tim21.R;
import com.example.booking_ma_tim21.activities.AccountActivity;
import com.example.booking_ma_tim21.activities.LoginActivity;
import com.example.booking_ma_tim21.activities.MainActivity;

public class NavigationSetup {
    public static void setupNavigation(final Activity activity) {

        DrawerLayout drawerLayout = activity.findViewById(R.id.drawerLayout);
        ImageView menu = activity.findViewById(R.id.menu);

        LinearLayout main = activity.findViewById(R.id.mainScreen);
        LinearLayout account = activity.findViewById(R.id.accountScreen);
        LinearLayout logout = activity.findViewById(R.id.loginScreen);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDrawer(drawerLayout);
            }
        });

        main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(activity, MainActivity.class);
            }
        });

        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(activity, AccountActivity.class);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(activity, LoginActivity.class);
            }
        });
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
