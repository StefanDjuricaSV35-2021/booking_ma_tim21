package com.example.booking_ma_tim21.dto;

import com.example.booking_ma_tim21.model.enumeration.AccommodationType;
import com.example.booking_ma_tim21.model.enumeration.Amenity;
import com.example.booking_ma_tim21.model.enumeration.RequestStatus;

import java.util.List;
import java.util.Set;

public class AccommodationChangeRequestDTO {
    private Long id;
    private long requestCreationDate;
    private RequestStatus status;

    private Long accommodationId;
    private Long ownerId;
    private String name;
    private AccommodationType type;
    private int minGuests;
    private int maxGuests;
    private String description;
    private String location;
    private List<Amenity> amenities;
    private List<String> photos;
    private int daysForCancellation;
    private boolean perNight;
    private boolean autoAccepting;
    private boolean enabled;

    public AccommodationChangeRequestDTO(Long id, long requestCreationDate, RequestStatus status, Long accommodationId, Long ownerId, String name, AccommodationType type, int minGuests, int maxGuests, String description, String location, List<Amenity> amenities, List<String> photos, int daysForCancellation, boolean perNight, boolean autoAccepting, boolean enabled) {
        this.id = id;
        this.requestCreationDate = requestCreationDate;
        this.status = status;
        this.accommodationId = accommodationId;
        this.ownerId = ownerId;
        this.name = name;
        this.type = type;
        this.minGuests = minGuests;
        this.maxGuests = maxGuests;
        this.description = description;
        this.location = location;
        this.amenities = amenities;
        this.photos = photos;
        this.daysForCancellation = daysForCancellation;
        this.perNight = perNight;
        this.autoAccepting = autoAccepting;
        this.enabled = enabled;
    }

    public AccommodationChangeRequestDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getRequestCreationDate() {
        return requestCreationDate;
    }

    public void setRequestCreationDate(long requestCreationDate) {
        this.requestCreationDate = requestCreationDate;
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

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AccommodationType getType() {
        return type;
    }

    public void setType(AccommodationType type) {
        this.type = type;
    }

    public int getMinGuests() {
        return minGuests;
    }

    public void setMinGuests(int minGuests) {
        this.minGuests = minGuests;
    }

    public int getMaxGuests() {
        return maxGuests;
    }

    public void setMaxGuests(int maxGuests) {
        this.maxGuests = maxGuests;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<Amenity> getAmenities() {
        return amenities;
    }

    public void setAmenities(List<Amenity> amenities) {
        this.amenities = amenities;
    }

    public List<String> getPhotos() {
        return photos;
    }

    public void setPhotos(List<String> photos) {
        this.photos = photos;
    }

    public int getDaysForCancellation() {
        return daysForCancellation;
    }

    public void setDaysForCancellation(int daysForCancellation) {
        this.daysForCancellation = daysForCancellation;
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

    public boolean isAutoAccepting() {
        return autoAccepting;
    }

    public void setAutoAccepting(boolean autoAccepting) {
        this.autoAccepting = autoAccepting;
    }
}
