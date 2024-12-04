package com.deep.demo.dto;


import lombok.Data;

@Data
public class JournalUploadResponse
{
    private String successReport;
    private String errorReport;
    private Integer successCount;
    private Integer errorCount;
    private Integer requestNo;

}
