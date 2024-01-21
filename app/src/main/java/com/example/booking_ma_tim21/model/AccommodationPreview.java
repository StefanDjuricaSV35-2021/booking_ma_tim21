package com.example.booking_ma_tim21.model;

public class AccommodationPreview {
    String name;
    String location;
    String imageSrc;

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    Double price;

    public AccommodationPreview(String name, String location, String imageSrc,Double price) {
        this.name = name;
        this.location = location;
        this.imageSrc = imageSrc;
        this.price=price;
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
