package com.deep.demo.dto;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class RequestDto {
    private Integer requestNo;

    private LocalDateTime postDate;

    private String description;

    private String requestType;

    private String status;

    private String country;

    private String approverGroup1;

    private String approverGroup2;

    private String approverUser1;

    private String approverUser2;

    private String requestorName;

    private String userId;

    private String reason;

    private String rejectedReason;

    private String approvedUser1Status;

    private String approvedUser2Status;

    private String adminUser;

    private String adminDate;
}
