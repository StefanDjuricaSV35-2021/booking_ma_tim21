package com.example.booking_ma_tim21.dto;

public class UserReportDTO {
    private Long id;
    private Long reportedId;
    private Long reporterId;
    private String description;

    public UserReportDTO(Long id, Long reportedId, Long reporterId, String description) {
        this.id = id;
        this.reportedId = reportedId;
        this.reporterId = reporterId;
        this.description = description;
    }

    public UserReportDTO() {
        // Default constructor with no parameters
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getReportedId() {
        return reportedId;
    }

    public void setReportedId(Long reportedId) {
        this.reportedId = reportedId;
    }

    public Long getReporterId() {
        return reporterId;
    }

    public void setReporterId(Long reporterId) {
        this.reporterId = reporterId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

