package com.example.booking_ma_tim21.model.enumeration;

public enum AccommodationType {
    Room,
    House,
    Condo,
    Apartment;

    public static AccommodationType fromString(String text) {
        for (AccommodationType type : AccommodationType.values()) {
            if (type.name().equalsIgnoreCase(text)) {
                return type;
            }
        }
        // Handle the case where the input string doesn't match any enum value
        return null;
    }
}
