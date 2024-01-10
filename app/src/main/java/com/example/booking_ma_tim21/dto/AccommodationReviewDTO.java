package com.example.booking_ma_tim21.dto;

public class AccommodationReviewDTO {
    private Long id;
    private Long reviewerId;
    private Long accommodationId;
    private String comment;
    private int rating;
    private Long timePosted;

    public AccommodationReviewDTO(Long id, Long reviewerId, Long accommodationId, String comment, int rating, Long timePosted) {
        this.id = id;
        this.reviewerId = reviewerId;
        this.accommodationId = accommodationId;
        this.comment = comment;
        this.rating = rating;
        this.timePosted = timePosted;
    }

    public AccommodationReviewDTO() {
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getReviewerId() {
        return reviewerId;
    }

    public void setReviewerId(Long reviewerId) {
        this.reviewerId = reviewerId;
    }

    public Long getAccommodationId() {
        return accommodationId;
    }

    public void setAccommodationId(Long accommodationId) {
        this.accommodationId = accommodationId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public Long getTimePosted() {
        return timePosted;
    }

    public void setTimePosted(Long timePosted) {
        this.timePosted = timePosted;
    }

}

