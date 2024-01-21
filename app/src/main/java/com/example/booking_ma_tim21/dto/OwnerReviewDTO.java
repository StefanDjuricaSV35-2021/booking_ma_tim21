package com.example.booking_ma_tim21.dto;

public class OwnerReviewDTO {
    private Long id;
    private Long reviewerId;
    private Long reviewedId;
    private String comment;
    private int rating;
    private Long timePosted;

    public OwnerReviewDTO(Long id, Long reviewerId, Long reviewedId, String comment, int rating, Long timePosted) {
        this.id = id;
        this.reviewerId = reviewerId;
        this.reviewedId = reviewedId;
        this.comment = comment;
        this.rating = rating;
        this.timePosted = timePosted;
    }

    public OwnerReviewDTO() {
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

    public Long getReviewedId() {
        return reviewedId;
    }

    public void setReviewedId(Long reviewedId) {
        this.reviewedId = reviewedId;
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
