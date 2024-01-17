package com.example.booking_ma_tim21.dto;

import com.example.booking_ma_tim21.model.TimeSlot;
import com.example.booking_ma_tim21.model.enumeration.ReservationRequestStatus;

public class ReservationRequestDTO {

    private Long id;
    private Long userId;
    private Long accommodationId;
    private int guestsNumber;
    private double price;
    private TimeSlot timeSlot;
    private ReservationRequestStatus status;

    public ReservationRequestDTO(Long userId, Long accommodationId, int guestsNumber, double price, TimeSlot timeSlot, ReservationRequestStatus status) {
        this.userId = userId;
        this.accommodationId = accommodationId;
        this.guestsNumber = guestsNumber;
        this.price = price;
        this.timeSlot = timeSlot;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getAccommodationId() {
        return accommodationId;
    }

    public int getGuestsNumber() {
        return guestsNumber;
    }

    public double getPrice() {
        return price;
    }

    public TimeSlot getTimeSlot() {
        return timeSlot;
    }

    public ReservationRequestStatus getStatus() {
        return status;
    }
}
