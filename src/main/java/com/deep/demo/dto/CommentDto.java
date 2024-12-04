package com.deep.demo.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
@Data
public class CommentDto {
    private String requestNo;
    private String comment;
    private LocalDateTime commentDate;
    private String userId;
}
