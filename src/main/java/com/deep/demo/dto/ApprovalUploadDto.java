package com.deep.demo.dto;

import lombok.Data;

import java.util.List;

@Data
public class ApprovalUploadDto {
    private List<ApprovalDto> approvalData;
}
