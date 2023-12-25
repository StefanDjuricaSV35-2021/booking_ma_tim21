package com.example.booking_ma_tim21.model;

import java.sql.Time;

public class AccommodationPricing {
    private Long id;
    private Long accommodationId;
    private TimeSlot timeSlot;
    private double price;

    public AccommodationPricing(Long id, Long accommodationId, TimeSlot timeSlot, double price) {
        this.id = id;
        this.accommodationId = accommodationId;
        this.timeSlot = timeSlot;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAccommodationId() {
        return accommodationId;
    }

    public void setAccommodationId(Long accommodationId) {
        this.accommodationId = accommodationId;
    }

    public TimeSlot getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(TimeSlot timeSlot) {
        this.timeSlot = timeSlot;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
