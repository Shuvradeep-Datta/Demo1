package com.deep.demo.dto;


import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class JournalEntryResponse {

    private Integer identifier;
    private String status = "";;
    private String title = "";
    private List<String> segment = new ArrayList<>();
    private List<Integer> expectedPeriod = new ArrayList<>();
    private List<String> centerDepartment = new ArrayList<>();
    private List<String> companyCode = new ArrayList<>();
    private List<String> dataSources = new ArrayList<>();
    private List<String> downstreamImpact = new ArrayList<>();
    private List<String> functionalArea = new ArrayList<>();
    private List<String> glAccount = new ArrayList<>();
    private List<String> upstreamDependency =new ArrayList<>();
    private String journalEntryCategory = "";
    private String journalEntryNature = "";
    private String journalEntryMethod = "" ;
    private String country = "";
    private String processingTeam = "";
    private String description = "";
    private String frequency = "";
    private String approverUserId = "";
    private String preparerUserId = "";
    private String reviewerUserId = "";
    private String accountableBusinessLine = "";
    private String accountableBusinessLineUserId ="";
    private String ebsMiddleOfficeUserId = "";
    private String dataSourcesDescription = "";
    private String upstreamDependencyDescriptio="";
    private String downstreamImpactDescription="";
    private String fsImpact = "";
    private Integer entryWorkday;
    private String entryWorkdayTime = "";
    private String intercompanyIndicator = "";
    private String cogsImpacting = "";
    private String soxControlNumber = "";
    private String automationTool = "";
    private String automationNotes = "";
    private String eliminationNotes = "";
    private String reasonForInactive ="";
    private  String inactiveDate = "";
    private  String targetLedgerSystem ="";

}
