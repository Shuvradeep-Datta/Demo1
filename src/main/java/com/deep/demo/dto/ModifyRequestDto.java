package com.deep.demo.dto;

import lombok.Data;

@Data
public class ModifyRequestDto {
    private String requestNo;
    private String postDate;
    private String reason;
    private String requestType;
    private String status;
    private String country;
    private String approverGroup1;
    private String approverUser1;
    private String requestorName;
    private String userId;
    private String approverGroup2;
    private String approverUser2;

}
