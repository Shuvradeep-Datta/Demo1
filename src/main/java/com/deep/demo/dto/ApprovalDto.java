package com.deep.demo.dto;

import lombok.Data;

@Data
public class ApprovalDto {
    private Integer requestNo;
    private String postDate;
    private String status;
    private String rejectedReason;
    private String reviewUser;
    private String flag;
    private String approvedUser1Status;
    private String approvedUser2Status;

}
