package com.example.booking_ma_tim21.authentication;

import android.content.SharedPreferences;
import android.content.Context;
import android.util.Log;

import com.example.booking_ma_tim21.model.JWTAuthenticationResponse;

import java.util.List;
import java.util.Map;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

public class AuthManager {
    private static AuthManager instance;
    private static String secret = "413F4428472B4B6250655368566D5970337336763979244226452948404D6351";
    private String userRole;
    private String userId;
    private SharedPreferences sharedPreferences;

    private AuthManager(Context context) {
        sharedPreferences = context.getSharedPreferences("auth", Context.MODE_PRIVATE);
    }

    public static AuthManager getInstance(Context context) {
        if (instance == null) {
            instance = new AuthManager(context);
        }
        return instance;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserRole() {
        return userRole;
    }

    public boolean isLoggedIn() {
        return userRole != null;
    }

    public void addUser(JWTAuthenticationResponse token){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("user", token.getToken());
        editor.apply();
        setUser();
    }

    public void setUser(){
        String accessToken = sharedPreferences.getString("user", "");

        if(!accessToken.isEmpty()){
            String id = getUserIdFromToken(accessToken);
            if(id!=null){
                this.userId = id;
                setUserRole();
            }
        }
    }

    public void setUserRole(){
        String accessToken = sharedPreferences.getString("user", "");
        if(!accessToken.isEmpty()){
            String role = getRoleFromToken(accessToken);
            if(role!=null){
                this.userRole = role;
            }
        }
    }

    public String getRoleFromToken(String accessToken) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secret)
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();

            Object rolesObject = claims.get("role");
            if (rolesObject instanceof List) {
                List<Map<String, String>> roleMaps = (List<Map<String, String>>) rolesObject;
                if (!roleMaps.isEmpty()) {
                    return roleMaps.get(0).get("authority");
                }
            }

            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getUserIdFromToken(String accessToken) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secret)
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();

            return claims.get("sub", String.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
