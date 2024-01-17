package com.example.booking_ma_tim21.model;

import com.example.booking_ma_tim21.dto.UserDTO;

public class UserReportWithEmails {
    private Long id;
    private UserDTO reportedUser;
    private UserDTO reporterUser;

    private String description;

    public UserReportWithEmails(Long id ,UserDTO reportedUser, UserDTO reporterUser, String description) {
        this.id = id;
        this.reportedUser = reportedUser;
        this.reporterUser = reporterUser;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserDTO getReportedUser() {
        return reportedUser;
    }

    public void setReportedUser(UserDTO reportedUser) {
        this.reportedUser = reportedUser;
    }

    public UserDTO getReporterUser() {
        return reporterUser;
    }

    public void setReporterUser(UserDTO reporterUser) {
        this.reporterUser = reporterUser;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
