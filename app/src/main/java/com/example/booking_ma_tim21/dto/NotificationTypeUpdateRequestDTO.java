package com.example.booking_ma_tim21.dto;

import java.util.List;

public class NotificationTypeUpdateRequestDTO {
    private Long userId;
    private List<NotificationType> subscribedNotificationTypes;

    public NotificationTypeUpdateRequestDTO(Long userId, List<NotificationType> subscribedNotificationTypes) {
        this.userId = userId;
        this.subscribedNotificationTypes = subscribedNotificationTypes;
    }

    public NotificationTypeUpdateRequestDTO() {
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<NotificationType> getSubscribedNotificationTypes() {
        return subscribedNotificationTypes;
    }

    public void setSubscribedNotificationTypes(List<NotificationType> subscribedNotificationTypes) {
        this.subscribedNotificationTypes = subscribedNotificationTypes;
    }
}
