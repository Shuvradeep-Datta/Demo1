package com.deep.demo.service;

import com.deep.demo.dto.JournalEntry;
import com.deep.demo.enums.CSVHeaders;
import com.deep.demo.enums.CSVHeaders17;
import com.deep.demo.repository.AdminRepository;
import com.deep.demo.repository.JournalRepository;
import com.opencsv.exceptions.CsvValidationException;
import lombok.Data;
import lombok.RequiredArgsConstructor;


import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

import static com.deep.demo.enums.CSVHeaders.SEGMENT;
import static com.deep.demo.enums.CSVHeaders10.DESCRIPTION;
import static com.deep.demo.enums.CSVHeaders11.FREQUENCY;
import static com.deep.demo.enums.CSVHeaders12.JOURNAL_PREPARER_USERID;
import static com.deep.demo.enums.CSVHeaders13.JOURNAL_APPROVER_USERID;
import static com.deep.demo.enums.CSVHeaders14.JOURNAL_REVIEWER_USERID;
import static com.deep.demo.enums.CSVHeaders15.ACC_BUSINESS_LINE;
import static com.deep.demo.enums.CSVHeaders16.ACC_BUSINESS_USERID;
import static com.deep.demo.enums.CSVHeaders17.UPSTREAM_DEPENDENCY;
import static com.deep.demo.enums.CSVHeaders2.TITLE;
import static com.deep.demo.enums.CSVHeaders3.JOURNAL_IDENTIFIER;
import static com.deep.demo.enums.CSVHeaders4.TARGET_LEDGER_SYSTEM_ID;
import static com.deep.demo.enums.CSVHeaders5.COUNTRY;
import static com.deep.demo.enums.CSVHeaders6.PROCESSING_TEAM;
import static com.deep.demo.enums.CSVHeaders7.JOURNAL_ENTRY_CATEGORY;
import static com.deep.demo.enums.CSVHeaders8.JOURNAL_ENTRY_NATURE;
import static com.deep.demo.enums.CSVHeaders9.JOURNAL_ENTRY_METHOD;
import static java.util.Collections.replaceAll;

@Service
@RequiredArgsConstructor
@Data
public class UploadJournalService {

    private final AdminRepository adminRepository;
    private final JournalRepository journalRepository;


    public Map<String, Integer> country = new HashMap<>();
    public Map<String, Integer> segment = new HashMap<>();
    public Map<String, Integer> targetLedgerSystem = new HashMap<>();
    public Map<String, Integer> processingTeam = new HashMap<>();
    public Map<String, Integer> journalCategory = new HashMap<>();
    public Map<String, Integer> journalNature = new HashMap<>();
    public Map<String, Integer> journalMethods = new HashMap<>();
    public Map<String, Integer> frequency = new HashMap<>();
    public Map<String, Integer> fsImpact = new HashMap<>();
    public Map<String, Integer> entryWorkDay = new HashMap<>();
    public Map<String, Integer> automationTool = new HashMap<>();
    public Map<String, Integer> accBusinessLine = new HashMap<>();
    public Map<String, Integer> interCompanyIndicator = new HashMap<>();
    public Map<String, Integer> dataSource = new HashMap<>();
    public Map<String, Integer> upStreamDependency = new HashMap<>();
    public Map<String, Integer> downstreamImpact = new HashMap<>();


    public void saveJournal(JournalEntry journalEntry) throws CsvValidationException {
        Integer journalId = Integer.valueOf(journalEntry.getLine()[1]);
        if (journalEntry.getLine()[0].equals("I")) {
            journalRepository.saveJournal(getParameters(journalEntry.getLine()));
        } else {
            int noOfRowsUpdated = journalRepository.updateJournal(getParameters(journalEntry.getLine()));
            if (noOfRowsUpdated > 0) {
                deleJournals(journalId);
            } else
                throw new CsvValidationException("JournalIdentifier not present in DB please change the action to I");

        }
        journalRepository.saveDataSourceById(journalId, journalEntry.getDataSource());

        journalRepository.saveSegmentById(journalId, journalEntry.getSegment());

        journalRepository.saveSegmentById(journalId, journalEntry.getDownStreamImpact());

        journalRepository.saveUpstreamDependencyById(journalId, journalEntry.getUpStreamDependency());

        journalRepository.saveExpectedPeriodById(journalId, journalEntry.getExpectedPeriod());

        journalRepository.saveCompanyCodeById(journalId, journalEntry.getCompanyCode());

        journalRepository.saveGlAccountById(journalId, journalEntry.getGlAccount());

        journalRepository.saveFunctionalAreaById(journalId, journalEntry.getFunctionalArea());

        journalRepository.saveCenterDepartmentById(journalId, journalEntry.getDepartment());

    }

    private void deleJournals(Integer journalId) {
        journalRepository.deleteDataSourceById(journalId);

        journalRepository.deleteSegmentById(journalId);
        journalRepository.deleteDownStreamImpactById(journalId);

        journalRepository.deleteUpstreamDependencyById(journalId);

        journalRepository.deleteExpectedPeriodById(journalId);

        journalRepository.deleteCompanyCodeById(journalId);

        journalRepository.deleteGlAccountById(journalId);

        journalRepository.deleteFunctionalAreaById(journalId);

        journalRepository.deleteCenterDepartmentById(journalId);
    }


    public SqlParameterSource getParameters(String[] line) {
        return new MapSqlParameterSource()
                .addValue("identifier", Integer.valueOf(line[JOURNAL_IDENTIFIER.getPosition()].trim()))
                .addValue("title", line[TITLE.getPosition()].replaceAll("\\s+", "").trim())
                .addValue("targetLedgerSystemId", this.targetLedgerSystem.get(line[TARGET_LEDGER_SYSTEM_ID.getPosition()].trim()))
                .addValue("countryId", country.get(line[COUNTRY.getPosition()].trim()))
                .addValue( "processingTeamId", processingTeam.get(line [PROCESSING_TEAM.getPosition()].trim()))
                .addValue("categoryId", journalCategory.get(line [JOURNAL_ENTRY_CATEGORY.getPosition()].trim()))
                .addValue("natureId", journalNature.get(line[JOURNAL_ENTRY_NATURE.getPosition()].trim()))
                .addValue("methodId", journalMethods.get(line[JOURNAL_ENTRY_METHOD.getPosition()].trim()))
                .addValue("description", line [DESCRIPTION.getPosition()].replaceAll("\\s+","").trim())
                .addValue("frequencyId", frequency.get(line [FREQUENCY.getPosition()].trim()))
                .addValue("preparerUserId", line [JOURNAL_PREPARER_USERID.getPosition()].trim())
                .addValue("approverUserId", line [JOURNAL_APPROVER_USERID.getPosition()].trim())
                .addValue("reviewerUserId", line [JOURNAL_REVIEWER_USERID.getPosition()].trim())
                .addValue("accountableBusinessLineId", accBusinessLine.get(line[ACC_BUSINESS_LINE.getPosition()].trim()))
                .addValue("accountableBusinessLineUserId", line [ACC_BUSINESS_USERID.getPosition()].trim());
    }


}