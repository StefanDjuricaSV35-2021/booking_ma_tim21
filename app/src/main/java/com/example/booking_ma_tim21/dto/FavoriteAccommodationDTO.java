package com.example.booking_ma_tim21.dto;


public class FavoriteAccommodationDTO {
    private Long favoriteAccommodationId;

    public Long getAccommodationId() {
        return accommodationId;
    }

    public void setAccommodationId(Long accommodationId) {
        this.accommodationId = accommodationId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    private Long accommodationId;
    private Long userId;


}

