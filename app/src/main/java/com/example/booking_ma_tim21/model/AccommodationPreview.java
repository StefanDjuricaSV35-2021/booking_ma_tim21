package com.example.booking_ma_tim21.model;

public class AccommodationPreview {
    String name;
    String location;
    String imageSrc;

    public AccommodationPreview(String name, String location, String imageSrc) {
        this.name = name;
        this.location = location;
        this.imageSrc = imageSrc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getImageSrc() {
        return imageSrc;
    }

    public void setImageUrl(String imageUrl) {
        this.imageSrc = imageUrl;
    }
}
