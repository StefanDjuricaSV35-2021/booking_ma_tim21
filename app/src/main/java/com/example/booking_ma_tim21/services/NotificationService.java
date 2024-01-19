package com.example.booking_ma_tim21.services;

import static android.app.PendingIntent.FLAG_IMMUTABLE;

import io.reactivex.schedulers.Schedulers;
import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioAttributes;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.JsonWriter;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.booking_ma_tim21.MyApplication;
import com.example.booking_ma_tim21.R;
import com.example.booking_ma_tim21.activities.MainActivity;
import com.example.booking_ma_tim21.authentication.AuthManager;
import com.example.booking_ma_tim21.dto.NotificationDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.gson.Gson;

import java.net.URISyntaxException;

import io.reactivex.schedulers.Schedulers;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;

public class NotificationService extends Service {

    private Binder binder;
    private static StompClient mStompClient;
    private static AuthManager authManager;
    private static final String CHANNEL_ID = "notifs";

    private static NotificationService instance;

    public NotificationService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance=this;
        binder = new Binder();
    }

    public static NotificationService getInstance(){
        if(instance==null){

            instance=new NotificationService();
        }
        return instance;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("MESS", "Notification started");
        String input = intent.getStringExtra("inputExtra");
        authManager = AuthManager.getInstance(this);
        createNotificationChannel();
        initializeWebSocketConnection();

        return START_NOT_STICKY;
    }


    public void initializeWebSocketConnection() {
        String serverUrl = "ws://10.0.2.2:8080/api/v1/auth/socket/websocket";

        mStompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, serverUrl);

        try {
            mStompClient.connect();

        } catch (IllegalStateException e) {
            // Log the details of the exception
            e.printStackTrace();
        }
    }

    private void createNotificationChannel() {

        Uri sound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + this.getPackageName() + "/" + R.raw.don_pollo);
        Log.d("AA",sound.toString());
        AudioAttributes attributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            serviceChannel.setSound(sound,attributes);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }

    public void openSocket() {
        String loggedUserId = authManager.getUserIdLong().toString();
        String destination = "/user/" + loggedUserId + "/specific";

        mStompClient.topic(destination).subscribe(topicMessage -> {
            Log.d("MESS", topicMessage.getPayload().toString());
            showNotification(topicMessage.getPayload());
        });

    }

    public void closeSocket() {
        mStompClient.disconnect();
    }


    public void showNotification(String message) {
        Uri sound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + this.getPackageName() + "/" + R.raw.don_pollo);

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, FLAG_IMMUTABLE);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("New notification")
                .setContentText(message)
                .setSmallIcon(R.drawable.idwin)
                .setSound(null) // Set custom sound here
                .setContentIntent(pendingIntent)
                .build();

        Ringtone r = RingtoneManager.getRingtone(this, sound);
        r.play();


        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }


        notificationManager.notify(0, notification);
    }

    public void sendNotification(NotificationDTO dto){
        Gson gson = new Gson();
        String json = gson.toJson(dto);
        mStompClient.send("/notifications-ws/send", json).subscribe();
    }



    @Override
    public void onDestroy() {
        // Here is a good place to stop the LocationEngine.
        super.onDestroy();
    }


}