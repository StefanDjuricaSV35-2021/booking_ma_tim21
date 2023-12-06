package com.example.booking_ma_tim21.dto;

public class AccommodationPreviewDTO {
    private Long id;
    private String name;
    private String location;
    private String image;

    public AccommodationPreviewDTO(Long id, String name, String location, String image) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.image = image;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getImage() {
        return image;
    }
}
