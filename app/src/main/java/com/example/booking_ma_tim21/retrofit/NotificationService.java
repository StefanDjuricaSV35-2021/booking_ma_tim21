package com.example.booking_ma_tim21.retrofit;

import static androidx.constraintlayout.widget.Constraints.TAG;

import android.content.Context;
import android.util.Log;


import com.example.booking_ma_tim21.authentication.AuthManager;
import com.example.booking_ma_tim21.dto.NotificationDTO;
import com.google.gson.Gson;

import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.client.StompClient;

public class NotificationService {
    private final Context context;
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
    }

    public void initializeWebSocketConnection() throws URISyntaxException {
        String serverUrl = "ws://10.0.2.2:8080/api/v1/auth/socket";

            mStompClient = Stomp.over(WebSocket.class,serverUrl);
            mStompClient.connect();
    }

    public void openSocket() {
        String loggedUserId = auth.getUserIdLong().toString();
        String destination = "/user/" + loggedUserId + "/specific";

        mStompClient.topic(destination).subscribe(topicMessage -> {
            Log.d(TAG, topicMessage.getPayload());
        });


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
}
