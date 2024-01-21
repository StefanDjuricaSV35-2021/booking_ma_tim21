package com.example.booking_ma_tim21.dto;

public class NotificationDTO {

    private Long id;
    NotificationType type;
    String message;
    Long recipientId;


    public NotificationDTO(Long id, String message) {
    }

    public NotificationDTO(Long id, NotificationType type, String message, Long recipientId) {
        this.id = id;
        this.type = type;
        this.message = message;
        this.recipientId = recipientId;
    }

    public NotificationDTO(NotificationType type, String message, Long recipientId) {
        this.type = type;
        this.message = message;
        this.recipientId = recipientId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(Long recipientId) {
        this.recipientId = recipientId;
    }
}

