package com.deep.demo.dto;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class AdminRevDto {
    private String approverGroup1;
    private String approverGroup2;
    private String approverUser1;
    private String approverUser2;
    private String approver1Status;
    private String approver2Status;

    private String requestNo;
    private String adminUser;
    private LocalDateTime adminDate;

    private String rejectedReason;


}
