package com.example.booking_ma_tim21.model;

import com.example.booking_ma_tim21.model.enumeration.AccommodationType;
import com.example.booking_ma_tim21.model.enumeration.Amenity;

import java.util.List;

public class Accommodation {
    private Long id;
    private Long ownerId;
    private String name;
    private AccommodationType type;
    private int minGuests;
    private int maxGuests;
    private String description;
    private List<Amenity> amenities;
    private List<String> photos;
    private int daysForCancellation;
    private boolean enabled;
    private boolean perNight;
    private String location;
    private List<TimeSlot> dates;

    public List<TimeSlot> getDates() {
        return dates;
    }

    public void setDates(List<TimeSlot> dates) {
        this.dates = dates;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public boolean isPerNight() {
        return perNight;
    }

    public void setPerNight(boolean perNight) {
        this.perNight = perNight;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public int getDaysForCancellation() {
        return daysForCancellation;
    }

    public void setDaysForCancellation(int daysForCancellation) {
        this.daysForCancellation = daysForCancellation;
    }

    public List<String> getPhotos() {
        return photos;
    }

    public void setPhotos(List<String> photos) {
        this.photos = photos;
    }

    public List<Amenity> getAmenities() {
        return amenities;
    }

    public void setAmenities(List<Amenity> amenities) {
        this.amenities = amenities;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getMaxGuests() {
        return maxGuests;
    }

    public void setMaxGuests(int maxGuests) {
        this.maxGuests = maxGuests;
    }

    public int getMinGuests() {
        return minGuests;
    }

    public void setMinGuests(int minGuests) {
        this.minGuests = minGuests;
    }

    public AccommodationType getType() {
        return type;
    }

    public void setType(AccommodationType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
