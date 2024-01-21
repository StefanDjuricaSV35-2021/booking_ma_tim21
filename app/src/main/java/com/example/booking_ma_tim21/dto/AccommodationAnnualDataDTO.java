package com.example.booking_ma_tim21.dto;

public class AccommodationAnnualDataDTO {
    String name;
    double[] profit;

    public String getName() {
        return name;
    }

    public double[] getProfit() {
        return profit;
    }

    public int[] getReservations() {
        return reservations;
    }

    int[] reservations;
}
