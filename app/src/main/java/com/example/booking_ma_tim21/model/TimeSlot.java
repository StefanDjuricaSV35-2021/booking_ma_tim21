package com.example.booking_ma_tim21.model;

import java.io.Serializable;

public class TimeSlot implements Serializable {
    private long startDate;
    private long endDate;

    public long getStartDate() {
        return startDate;
    }

    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }

    public long getEndDate() {
        return endDate;
    }

    public void setEndDate(long endDate) {
        this.endDate = endDate;
    }

    public TimeSlot(long startDate, long endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public boolean overlapsWith(TimeSlot timeSlot) {
        return !(this.endDate < timeSlot.startDate || timeSlot.endDate < this.startDate);
    }
}
