package com.example.booking_ma_tim21.model;

public class AccommodationPreview {
    String name;
    String location;
    String price;
    int imageUrl;


    public AccommodationPreview(String name, String location, String price, int imageUrl) {
        this.name = name;
        this.location = location;
        this.price = price;
        this.imageUrl = imageUrl;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(int imageUrl) {
        this.imageUrl = imageUrl;
    }
}
