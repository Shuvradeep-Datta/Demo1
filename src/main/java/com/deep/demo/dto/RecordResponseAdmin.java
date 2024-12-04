package com.deep.demo.dto;

import lombok.Data;

@Data
public class RecordResponseAdmin {
    private Integer requestNo;
    private String postDate;
    private String description;
    private String requestType;
    private String status;
    private String country;
    private String approverGroup1;
    private String requestorName;
    private String approverUser1;
    private String approverUser2;
    private String approvedUser1Status;
    private String approvedUser2Status;

}
