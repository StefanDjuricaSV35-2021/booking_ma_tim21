package com.example.booking_ma_tim21.dto;

import com.example.booking_ma_tim21.model.TimeSlot;
import com.example.booking_ma_tim21.model.enumeration.AccommodationType;
import com.example.booking_ma_tim21.model.enumeration.Amenity;

import java.io.Serializable;
import java.util.List;

public class AccommodationDetailsDTO implements Serializable {

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

    public AccommodationDetailsDTO(Long id, Long ownerId, String name, AccommodationType type,
                                   int minGuests, int maxGuests, String description, List<Amenity> amenities,
                                   List<String> photos, int daysForCancellation, boolean enabled,
                                   boolean perNight, String location, List<TimeSlot> dates) {
        this.id = id;
        this.ownerId = ownerId;
        this.name = name;
        this.type = type;
        this.minGuests = minGuests;
        this.maxGuests = maxGuests;
        this.description = description;
        this.amenities = amenities;
        this.photos = photos;
        this.daysForCancellation = daysForCancellation;
        this.enabled = enabled;
        this.perNight = perNight;
        this.location = location;
        this.dates = dates;
    }

    public Long getId() {
        return id;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public String getName() {
        return name;
    }

    public AccommodationType getType() {
        return type;
    }

    public int getMinGuests() {
        return minGuests;
    }

    public int getMaxGuests() {
        return maxGuests;
    }

    public String getDescription() {
        return description;
    }

    public List<Amenity> getAmenities() {
        return amenities;
    }

    public List<String> getPhotos() {
        return photos;
    }

    public int getDaysForCancellation() {
        return daysForCancellation;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public boolean isPerNight() {
        return perNight;
    }

    public String getLocation() {
        return location;
    }


    public List<TimeSlot> getDates() {
        return dates;
    }
}
