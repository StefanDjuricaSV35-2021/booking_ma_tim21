package com.example.booking_ma_tim21.dto;

import com.example.booking_ma_tim21.model.TimeSlot;
import com.example.booking_ma_tim21.model.enumeration.RequestStatus;

public class AccommodationPricingChangeRequestDTO {
    private Long id;
    private Long accommodationChangeRequestId;
    private RequestStatus status;
    private Long accommodationId;
    private TimeSlot timeSlot;
    private double price;

    public AccommodationPricingChangeRequestDTO(Long id, Long accommodationChangeRequestId, RequestStatus status, Long accommodationId, TimeSlot timeSlot, double price) {
        this.id = id;
        this.accommodationChangeRequestId = accommodationChangeRequestId;
        this.status = status;
        this.accommodationId = accommodationId;
        this.timeSlot = timeSlot;
        this.price = price;
    }

    public AccommodationPricingChangeRequestDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAccommodationChangeRequestId() {
        return accommodationChangeRequestId;
    }

    public void setAccommodationChangeRequestId(Long accommodationChangeRequestId) {
        this.accommodationChangeRequestId = accommodationChangeRequestId;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
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
