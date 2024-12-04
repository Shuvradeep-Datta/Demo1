package com.deep.demo.repository;


import com.deep.demo.dto.AdminDto;

import java.util.List;

public interface AdminRepository {

    List<AdminDto> getSegments();

    List<AdminDto> getCountries();

    List<AdminDto> getTargetLedgerSystems();

    List<AdminDto> getProcessingTeams();

    List<AdminDto> getJournalCategories();

    List<AdminDto> getJournalMethods();

    List<AdminDto> getJournalNature();

    List<AdminDto> getFrequencies();

    List<AdminDto> getFSImpact();

    List<AdminDto> getExpectedEntryWorkdays();

    List<AdminDto> getInterCompanyIndicator();

    List<AdminDto> getAutomationTool();

    List<AdminDto> getAccountableBusinessLine();

    List<AdminDto> getDataSource();

    List<AdminDto> getUpStreamDependency();

    List<AdminDto> getDownStreamImpact();

    void addSegments(List<AdminDto> adminUploadDto);

    void addDataSources(List<AdminDto> adminUploadDto);

    void addDownStreamImpact(List<AdminDto> adminUploadDto);

    void addUpstreamDependency(List<AdminDto> adminUploadDto);

    void addExpectedEntryWorkDay(List<AdminDto> adminUploadDto);

    void addAccountableBusinessLine(List<AdminDto> adminUploadDto);

    void addAutomationTool(List<AdminDto> adminUploadDto);

    void addCountry(List<AdminDto> adminUploadDto);

    void addFrequency(List<AdminDto> adminUploadDto);

    void addFsImpact(List<AdminDto> adminUploadDto);

    void addIntercompanyIndicator(List<AdminDto> adminUploadDto);

    void addJournalEntryCategory(List<AdminDto> adminUploadDto);

    void addJournalEntryMethod(List<AdminDto> adminUploadDto);

    void addJournalEntryNature(List<AdminDto> adminUploadDto);

    void addProcessingTeam(List<AdminDto> adminUploadDto);

    void addTargetLedgerSystem(List<AdminDto> adminUploadDto);

    void deleteSegments(String deleteId);

    void deleteDataSources(String deleteId);

    void deleteDownStreamImpact(String deleteId);

    void deleteUpstreamDependency(String deleteId);

    void deleteExpectedEntryWorkDay(String deleteId);

    void deleteAccountableBusinessLine(String deleteId);

    void deleteAutomationTool(String deleteId);

    void deleteCountry(String deleteId);

    void deleteFrequency(String deleteId);

    void deleteFsImpact(String deleteId);

    void deleteIntercompanyIndicator(String deleteId);

    void deleteJournalEntryCategory(String deleteId);

    void deleteJournalEntryMethod(String deleteId);

    void deleteJournalEntryNature(String deleteId);

    void deleteProcessingTeam(String deleteId);

    void deleteTargetLedgerSystem(String deleteId);

    void updateSegments(String segmentId, AdminDto adminUpdateDto);

    void updateDataSources(String dataSourceId, AdminDto adminUpdateDto);

    void updateDownStreamImpact(String downstreamImpactId, AdminDto adminUpdateDto);

    void updateUpstreamDependency(String upstreamDependencyId, AdminDto adminUpdateDto);

    void updateExpectedEntryWorkDay(String expectedEntryWorkDayId, AdminDto adminUpdateDto);

    void updateAccountableBusinessLine(String accountableBusinessLineId, AdminDto adminUpdateDto);

    void updateAutomationTool(String automationToolId, AdminDto adminUpdateDto);

    void updateCountry(String countryId, AdminDto adminUpdateDto);

    void updateFrequency(String frequencyId, AdminDto adminUpdateDto);

    void updateFsImpact(String fsImpactId, AdminDto adminUpdateDto);

    void updateIntercompanyIndicator(String intercompanyIndicatorId, AdminDto adminUpdateDto);

    void updateJournalEntryCategory(String journalEntryCategoryId, AdminDto adminUpdateDto);

    void updateJournalEntryMethod(String journalEntryMethodId, AdminDto adminUpdateDto);

    void updateJournalEntryNature(String journalEntryNatureId, AdminDto adminUpdateDto);

    void updateProcessingTeam(String processingTeamId, AdminDto adminUpdateDto);

    void updateTargetLedgerSystem(String targetLedgerSystemId, AdminDto adminUpdateDto);
}



