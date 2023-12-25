package com.example.booking_ma_tim21.model;

public class JWTAuthenticationResponse {
    private String token;

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public JWTAuthenticationResponse() {
    }

    public JWTAuthenticationResponse(String token) {
        this.token = token;
    }
}
