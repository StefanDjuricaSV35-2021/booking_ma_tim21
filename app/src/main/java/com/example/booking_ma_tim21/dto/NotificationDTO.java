package com.example.booking_ma_tim21.dto;

public class NotificationDTO {

    private Long id;
    NotificationType type;
    String message;
    Long recipientId;


    public NotificationDTO(Long id, String message) {
    }
}

