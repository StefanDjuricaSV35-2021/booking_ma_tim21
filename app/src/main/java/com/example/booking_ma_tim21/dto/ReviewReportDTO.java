package com.example.booking_ma_tim21.dto;

public class ReviewReportDTO {
    private Long id;
    private Long reportedReviewId;
    private Long reporterId;

    public ReviewReportDTO(Long id, Long reportedReviewId, Long reporterId) {
        this.id = id;
        this.reportedReviewId = reportedReviewId;
        this.reporterId = reporterId;
    }

    public ReviewReportDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getReportedReviewId() {
        return reportedReviewId;
    }

    public void setReportedReviewId(Long reportedReviewId) {
        this.reportedReviewId = reportedReviewId;
    }

    public Long getReporterId() {
        return reporterId;
    }

    public void setReporterId(Long reporterId) {
        this.reporterId = reporterId;
    }
}

