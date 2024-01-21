package com.example.booking_ma_tim21.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class TimeSlot implements Serializable,Parcelable {
    private long startDate;
    private long endDate;

    protected TimeSlot(Parcel in) {
        startDate = in.readLong();
        endDate = in.readLong();
    }


    public static final Creator<TimeSlot> CREATOR = new Creator<TimeSlot>() {
        @Override
        public TimeSlot createFromParcel(Parcel in) {
            return new TimeSlot(in);
        }

        @Override
        public TimeSlot[] newArray(int size) {
            return new TimeSlot[size];
        }
    };

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
        return !(this.endDate <= timeSlot.startDate || timeSlot.endDate <= this.startDate);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeLong(startDate);
        dest.writeLong(endDate);
    }
}
