package com.example.booking_ma_tim21;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.example.booking_ma_tim21.retrofit.NotificationService;

public class MyApplication extends Application implements NotificationService.NotificationTrigger {

    private static MyApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("REZ","Application start");
        //Parse SDK stuff goes here
    }

    public MyApplication() {
        instance = this;
    }

    public static MyApplication getAppContext() {
        return instance;
    }

    @Override
    public void displayNotif(String message) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            // We are on the main thread, safe to display the Toast
            showToast(message);
        } else {
            // We are not on the main thread, post to the main thread to display the Toast
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    showToast(message);
                }
            });
        }
    }

    private void showToast(String message) {
        // Replace 'getApplicationContext()' with the actual context if needed
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}
