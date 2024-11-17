package com.deep.demo.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JournalSearchDto {


    private List<Integer> categoryList;

    private List<Integer> natureList;

    private List<Integer> processingTeamList;

    private List<Integer> frequencyList;

    private List<Integer> methodList;

    private List<Integer> countryList;

    private List<Integer> fsImpactList;

    private List<Integer> interCmpIndList;

    private List<Integer> automationToolList;

    private List<Integer> targetLedgerSystemList;

    private List<Integer> accountableBusinessLineList;

    private List<Integer> expectedEntryWorkdayList;

    private List<Integer> dataSourceList;

    private List<Integer> segmentList;

    private List<Integer> downStreamImpactList;

    private List<Integer> upStreamDependencyList;

    private List<String> departmentList;

    private List<String> companyCodeList;

    private List<Integer> expectedPeriodList;

    private List<String> functionalArealist;

    private List<String> glAccountList;
}
