package com.deep.demo.dto;

import lombok.Data;

import java.util.List;

@Data
public class CommentUploadDto {

    private List<CommentUploadDto> commentData;
}
