package com.deep.demo.dto;

import lombok.Data;

@Data
public class RecordInfoResponse {

    private Integer requestNo;

    private String postDate;

    private String description;

    private String status;

    private String country;

    private String approverGroup1;

    private String approverGroup2;

    private String approverUser1;

    private String approverUser2;

    private String requestorName;

    private String reason;

    private String requestType;

    private String mjeFile;

    private String ajeFile;

    private String rejectedReason;

    private String ApprovedUser1Status;

    private String ApprovedUser2Status;

    private String adminUser;
    private String userld;
}
