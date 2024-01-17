package com.example.booking_ma_tim21.retrofit;

import static android.icu.number.NumberRangeFormatter.with;
import static androidx.constraintlayout.widget.Constraints.TAG;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;


import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.booking_ma_tim21.MyApplication;
import com.example.booking_ma_tim21.R;
import com.example.booking_ma_tim21.activities.MainActivity;
import com.example.booking_ma_tim21.authentication.AuthManager;
import com.example.booking_ma_tim21.dto.NotificationDTO;
import com.google.gson.Gson;


import org.java_websocket.WebSocket;

import java.net.URI;
import java.net.URISyntaxException;

import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;

public class NotificationService extends Service {



    public interface NotificationTrigger{

        void displayNotif(String message);

    }
    Context context;
    AuthManager auth;
    private StompClient mStompClient;
    static NotificationService instance=null;

    public static NotificationService getInstance(Context context){
        if(instance==null){
            instance=new NotificationService(context);
            return instance;
        }
        return instance;
    }

    public NotificationService( Context context) {
        this.auth=AuthManager.getInstance(context);
        this.context = context;
        initialize();
    }

    public void initialize() {
        try {
            initializeWebSocketConnection();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        openSocket();
    }

    public void initializeWebSocketConnection() throws URISyntaxException {
        String serverUrl = "ws://10.0.2.2:8080/api/v1/auth/socket/websocket";

            mStompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP,serverUrl);
        try {
            mStompClient.connect();

        } catch (IllegalStateException e) {
            // Log the details of the exception
            e.printStackTrace();
        }
    }

    public void openSocket() {
        String loggedUserId = auth.getUserIdLong().toString();
        String destination = "/user/" + loggedUserId + "/specific";

        mStompClient.topic(destination).subscribe(topicMessage -> {
            showNotification(topicMessage.getPayload().toString());
        });


    }

    public void showNotification (String message){

        MyApplication.getAppContext().displayNotif(message);
    }




    public void closeSocket() {
        mStompClient.disconnect();
        this.instance=null;
    }

    public void sendNotification(NotificationDTO notification) {
        Gson gson = new Gson();
        String notificationJson = gson.toJson(notification);

        mStompClient.send("/notifications-ws/send", notificationJson);
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
