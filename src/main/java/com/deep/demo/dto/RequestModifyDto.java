package com.deep.demo.dto;

import lombok.Data;

import java.util.List;

@Data
public class RequestModifyDto {
    private List<ModifyRequestDto> requestModifyData;
}
