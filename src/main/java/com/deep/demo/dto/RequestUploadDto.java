package com.deep.demo.dto;

import lombok.Data;

import java.util.List;

@Data
public class RequestUploadDto {
    private List<RequestDto> requestData;
}
