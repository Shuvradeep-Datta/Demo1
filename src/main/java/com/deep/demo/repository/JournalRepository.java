package com.deep.demo.repository;

import com.deep.demo.dto.JournalEntryResponse;
import com.deep.demo.dto.JournalSearchDto;
import org.apache.commons.collections.map.MultiValueMap;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;


import java.util.List;
import java.util.Map;

public interface JournalRepository {
    int getJournalEntryCount();


    Page<JournalEntryResponse> findAll(JournalSearchDto journalSearchDto, PageRequest pageable);

    Page<JournalEntryResponse> findAll(Pageable page, int count);

    MultiValueMap getJournalEntryMap(String companyCode, String companyCode1);

    MultiValueMap getJournalEntryMapForInteger(String expectedPeriod, String expectedPeriod1);

    void saveJournal(SqlParameterSource parameters);

    int updateJournal(SqlParameterSource parameters);

    void saveDataSourceById(Integer journalId, List<Integer> dataSource);

    void saveSegmentById(Integer journalId, List<Integer> segment);

    void saveUpstreamDependencyById(Integer journalId, List<Integer> upStreamDependency);

    void saveExpectedPeriodById(Integer journalId, List<Integer> expectedPeriod);

    void saveCompanyCodeById(Integer journalId, List<String> companyCode);

    void saveGlAccountById(Integer journalId, List<String> glAccount);

    void saveFunctionalAreaById(Integer journalId, List<String> functionalArea);

    void saveCenterDepartmentById(Integer journalId, List<String> department);

    void deleteDataSourceById(Integer journalId);

    void deleteSegmentById(Integer journalId);

    void deleteDownStreamImpactById(Integer journalId);

    void deleteUpstreamDependencyById(Integer journalId);

    void deleteExpectedPeriodById(Integer journalId);

    void deleteCompanyCodeById(Integer journalId);

    void deleteGlAccountById(Integer journalId);

    void deleteFunctionalAreaById(Integer journalId);

    void deleteCenterDepartmentById(Integer journalId);

    List<JournalEntryResponse> getJournalEntryById(Map<String, Number> parameters);


    void saveDownstreamImpactById(Integer identifier, List<Integer> integers);

    void deleteJournalById(Integer journalId);

    boolean getJournalTempEntry(Map<String, Number> parameters);

    boolean getJournalEntry(Map<String, Number> parameters);

    void updateModifiedJournal(SqlParameterSource parameters);

    void saveJournalModificationTable(SqlParameterSource modifiedParameters);
}
