package com.deep.demo.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class tempJournalResponse {
    private Integer identifier;
    private String status = "";
    private String title = "";
    private String categoryId = "";
    private String natureId = "";
    private String methodId = "";
    private String countryId = "";
    private String processingTeamId = "";
    private String description = "";
    private String frequencyId = "";
    private String approverUserId = "";
    private String preparerUserId = "";
    private String reviewerUserId = "";
    private String accountableBusinessLineId = "";
    private String accountableBusinessLineUserId = "";
    private String ebsMiddleOfficeUserId = "";
    private String dataSourcesDescription = "";
    private String upstreamDependencyDescription = "";
    private String downstreamImpactDescription = "";
    private String fsImpactId = "";
    private Integer expectedEntryWorkdayId;
    private String entryWorkdayTime = "";
    private String intercompanyIndicatorId = "";
    private String cogsImpacting = "";
    private String soxControlNumber = "";
    private String automationToolId = "";
    private String automationNotes = "";
    private String eliminationNotes = "";
    private String reasonForInactive = "";
    private String inactivatedDate = "";
    private String targetLedgerSystemId = "";
    private String RequestNo="";
    private String modification="";
    private List<String> segment= new ArrayList<>();
    private List<String> dataSources = new ArrayList<>();
    private List<String> upstreamDependency=new ArrayList<>();
    private List<String> downstreamImpact=new ArrayList<>();
    private List<String> expectedPeriod = new ArrayList<>();
    private List<String> glAccount=new ArrayList<>();
    private List<String> companyCode=new ArrayList<>();
    private List<String> centerDepartment=new ArrayList<>();
    private List<String> functionalArea=new ArrayList<>();
    private String rn="";
    private String certificationStatus="";
    private String certificationDate="";
    private String creationDate="";
    private Integer delayedBy;
}
