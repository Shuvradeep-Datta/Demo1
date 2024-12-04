package com.deep.demo.dto;

import lombok.Data;

@Data
public class RecordResponse {

    private Integer requestNo;
    private String postDate;
    private String description;
    private String requestType;
    private String status;
    private String country;
    private String approverGroup1;
    private String approverGroup2;
    private String approverUser1;
    private String approverUser2;

}
