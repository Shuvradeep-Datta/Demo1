package com.deep.demo.service;

import com.deep.demo.dto.AdminDto;
import com.deep.demo.dto.JournalEntry;
import com.deep.demo.repository.AdminRepository;
import com.deep.demo.repository.JournalRepository;
import com.deep.demo.repository.RequestRepository;
import com.opencsv.exceptions.CsvValidationException;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.deep.demo.constants.JournalQueriesConstants.FUNCTIONAL_AREA;
import static com.deep.demo.enums.CSVHeaders10.DESCRIPTION;
import static com.deep.demo.enums.CSVHeaders11.FREQUENCY;
import static com.deep.demo.enums.CSVHeaders12.JOURNAL_PREPARER_USERID;
import static com.deep.demo.enums.CSVHeaders13.JOURNAL_APPROVER_USERID;
import static com.deep.demo.enums.CSVHeaders14.JOURNAL_REVIEWER_USERID;
import static com.deep.demo.enums.CSVHeaders15.ACC_BUSINESS_LINE;
import static com.deep.demo.enums.CSVHeaders16.ACC_BUSINESS_USERID;
import static com.deep.demo.enums.CSVHeaders2.TITLE;
import static com.deep.demo.enums.CSVHeaders3.JOURNAL_IDENTIFIER;
import static com.deep.demo.enums.CSVHeaders4.TARGET_LEDGER_SYSTEM_ID;
import static com.deep.demo.enums.CSVHeaders5.COUNTRY;
import static com.deep.demo.enums.CSVHeaders6.PROCESSING_TEAM;
import static com.deep.demo.enums.CSVHeaders7.JOURNAL_ENTRY_CATEGORY;
import static com.deep.demo.enums.CSVHeaders8.JOURNAL_ENTRY_NATURE;
import static com.deep.demo.enums.CSVHeaders9.JOURNAL_ENTRY_METHOD;


@Service
@RequiredArgsConstructor
@Data
public class UploadJournalService {
    private final AdminRepository adminRepository;
    private final JournalRepository journalRepository;
    private final RequestRepository requestRepository;
    public Map<String, Integer> country;
    public Map<String, Integer> segment;
    public Map<String, Integer> targetLedgerSystem;
    public Map<String, Integer> processingTeam;
    public Map<String, Integer> journalCategory;
    public Map<String, Integer> journalNature;
    public Map<String, Integer> journalMethods;
    public Map<String, Integer> frequency;
    public Map<String, Integer> fsImpact;
    public Map<String, Integer> entryWorkDay;
    public Map<String, Integer> automationTool;
    public Map<String, Integer> accBusinessLine;
    public Map<String, Integer> interCompanyIndicator;
    public Map<String, Integer> dataSource;
    public Map<String, Integer> upStreamDependency;
    public Map<String, Integer> downStreamImpact;

    public void getAdminConfigTables() {
        country = adminRepository.getCountries().stream().collect(Collectors.toMap(AdminDto::getValue, AdminDto::getId));
        segment = adminRepository.getSegments().stream().collect(Collectors.toMap(AdminDto::getValue, AdminDto::getId));
        targetLedgerSystem = adminRepository.getTargetLedgerSystems().stream().collect(Collectors.toMap(AdminDto::getValue, AdminDto::getId));
        processingTeam = adminRepository.getProcessingTeams().stream().collect(Collectors.toMap(AdminDto::getValue, AdminDto::getId));
        journalCategory = adminRepository.getJournalCategories().stream().collect(Collectors.toMap(AdminDto::getValue, AdminDto::getId));
        journalNature = adminRepository.getJournalNature().stream().collect(Collectors.toMap(AdminDto::getValue, AdminDto::getId));
        journalMethods = adminRepository.getJournalMethods().stream().collect(Collectors.toMap(AdminDto::getValue, AdminDto::getId));
        frequency = adminRepository.getFrequencies().stream().collect(Collectors.toMap(AdminDto::getValue, AdminDto::getId));
        fsImpact = adminRepository.getFSImpact().stream().collect(Collectors.toMap(AdminDto::getValue, AdminDto::getId));
        entryWorkDay = adminRepository.getExpectedEntryWorkdays().stream().collect(Collectors.toMap(AdminDto::getValue, AdminDto::getId));
        automationTool = adminRepository.getAutomationTool().stream().collect(Collectors.toMap(AdminDto::getValue, AdminDto::getId));
        accBusinessLine = adminRepository.getAccountableBusinessLine().stream().collect(Collectors.toMap(AdminDto::getValue, AdminDto::getId));
        interCompanyIndicator = adminRepository.getInterCompanyIndicator().stream().collect(Collectors.toMap(AdminDto::getValue, AdminDto::getId));
        dataSource = adminRepository.getDataSource().stream().collect(Collectors.toMap(AdminDto::getValue, AdminDto::getId));
        upStreamDependency = adminRepository.getUpStreamDependency().stream().collect(Collectors.toMap(AdminDto::getValue, AdminDto::getId));
        downStreamImpact = adminRepository.getDownStreamImpact().stream().collect(Collectors.toMap(AdminDto::getValue, AdminDto::getId));
    }

    public void saveJournal(JournalEntry journalEntry, int requestNo, String source, boolean isRequestInfoSame) {
        Integer journalId = Integer.valueOf(journalEntry.getLine()[1]);

        Map<String, Number> parameters = Map.of("identifier", journalId);
        boolean isEntryInTemp = journalRepository.getJournalTempEntry(parameters);
        boolean isEntryInJournal = journalRepository.getJournalEntry(parameters);

        switch (journalEntry.getLine()[0]) {
            case "I":
                if ((isEntryInTemp || isEntryInJournal) && source.equals("create")) {
                    throw new RuntimeException("The journal entry is already present in use");
                } else if (!(isEntryInTemp || isEntryInJournal) && source.equals("create")) {
                    journalRepository.saveJournal(getParameters(journalEntry.getLine(), requestNo));
                } else if (isRequestInfoSame && source.equals("modifyRequest")) {
                    break;
                } else if (!isRequestInfoSame && source.equals("modifyRequest")) {
                    journalRepository.updateModifiedJournal(getParameters(journalEntry.getLine(), requestNo));
                } else if (source.equals("modifyJournal")) {
                    throw new RuntimeException("Action cannot be I for updating an existing entry");
                }
                break;
            case "U":
                if (source.equals("create") || source.equals("modifyRequest")) {
                    throw new RuntimeException("Action cannot be U for new Insertion.");
                } else if (isEntryInTemp || isEntryInJournal) {
                    journalRepository.updateModifiedJournal(getParameters(journalEntry.getLine(), requestNo));
                } else {
                    throw new RuntimeException("The journal entry is not present.");
                }
                break;
        }
    }

    public void saveJournalExpectedPeriod(String identifier, List<Integer> expectedJournals) {
        List<String> expectedJournalsAsString = expectedJournals.stream()
                .map(String::valueOf)
                .collect(Collectors.toList());
        String expectedJournalsString = String.join(", ", expectedJournalsAsString);
        requestRepository.saveExpectedPeriodToTemp(Integer.valueOf(identifier), expectedJournalsString);
    }

    public void saveJournalSegment(String identifier, List<Integer> segments) {
        List<String> segmentsAsString = segments.stream()
                .map(String::valueOf)
                .collect(Collectors.toList());
        String segmentsString = String.join(", ", segmentsAsString);
        requestRepository.saveSegmentToTemp(Integer.valueOf(identifier), segmentsString);
    }

    public void saveJournalDataSources(String identifier, List<Integer> dataSources) {
        List<String> dataSourcesAsString = dataSources.stream()
                .map(String::valueOf)
                .collect(Collectors.toList());
        String dataSourcesString = String.join(", ", dataSourcesAsString);
        requestRepository.saveDataSourcesToTemp(Integer.valueOf(identifier), dataSourcesString);
    }

    public void saveJournalUpstreamDependency(String identifier, List<Integer> upStreamDependency) {
        List<String> upStreamDependencyAsString = upStreamDependency.stream()
                .map(String::valueOf)
                .collect(Collectors.toList());
        String upStreamDependencyString = String.join(", ", upStreamDependencyAsString);
        requestRepository.saveUpstreamDependencyToTemp(Integer.valueOf(identifier), upStreamDependencyString);
    }

    public void saveJournalDownStreamImpact(String identifier, List<Integer> downStreamImpact) {
        List<String> downStreamImpactAsString = downStreamImpact.stream()
                .map(String::valueOf)
                .collect(Collectors.toList());
        String downStreamImpactString = String.join(", ", downStreamImpactAsString);
        requestRepository.saveDownStreamImpactToTemp(Integer.valueOf(identifier), downStreamImpactString);
    }

    public void saveJournalGlAccount(String identifier, List<String> glAccounts) {
        String glAccountsString = String.join(", ", glAccounts);
        requestRepository.saveGlAccountToTemp(Integer.valueOf(identifier), glAccountsString);
    }

    public void saveJournalFunctionalArea(String identifier, List<String> functionalAreas) {
        String functionalAreasString = String.join(", ", functionalAreas);
        requestRepository.saveFunctionalAreaToTemp(Integer.valueOf(identifier), functionalAreasString);
    }

    public void saveJournalDepartment(String identifier, List<String> departments) {
        String departmentsString = String.join(", ", departments);
        requestRepository.saveDepartmentToTemp(Integer.valueOf(identifier), departmentsString);
    }

    public void saveJournalCompanyCode(String identifier, List<String> companyCodes) {
        String companyCodesString = String.join(", ", companyCodes);
        requestRepository.saveCompanyCodeToTemp(Integer.valueOf(identifier), companyCodesString);
    }

    @Transactional
    public void saveJournalInModificationTable(JournalEntry journalEntry, int requestNo) throws CsvValidationException {
        journalRepository.saveJournalModificationTable(getModifiedParameters(journalEntry.getLine(), requestNo));
    }

    private SqlParameterSource getParameters(String[] line, int requestNo) {

        ZoneId zoneId = ZoneId.of("America/Chicago");
        LocalDateTime dateTimeFlag = LocalDateTime.now(zoneId);

        return new MapSqlParameterSource()
                .addValue("requestNo", requestNo)
                .addValue("creationDate", dateTimeFlag)
                .addValue("identifier", Integer.valueOf(line[JOURNAL_IDENTIFIER.getPosition()].trim()))
                .addValue("title", line[TITLE.getPosition()].replaceAll("\\s+", " ").trim())
                .addValue("targetLedgerSystemId", this.targetLedgerSystem.get(line[TARGET_LEDGER_SYSTEM_ID.getPosition()].trim()))
                .addValue("countryId", country.get(line[COUNTRY.getPosition()].trim()))
                .addValue("processingTeamId", processingTeam.get(line[PROCESSING_TEAM.getPosition()].trim()))
                .addValue("categoryId", journalCategory.get(line[JOURNAL_ENTRY_CATEGORY.getPosition()].trim()))
                .addValue("natureId", journalNature.get(line[JOURNAL_ENTRY_NATURE.getPosition()].trim()))
                .addValue("methodId", journalMethods.get(line[JOURNAL_ENTRY_METHOD.getPosition()].trim()))
                .addValue("description", line[DESCRIPTION.getPosition()].replaceAll("\\s+", " ").trim())
                .addValue("frequencyId", frequency.get(line[FREQUENCY.getPosition()].trim()))
                .addValue("preparerUserId", line[JOURNAL_PREPARER_USERID.getPosition()].trim())
                .addValue("approverUserId", line[JOURNAL_APPROVER_USERID.getPosition()].trim())
                .addValue("reviewerUserId", line[JOURNAL_REVIEWER_USERID.getPosition()].trim())
                .addValue("accountableBusinessLineId", accBusinessLine.get(line[ACC_BUSINESS_LINE.getPosition()].trim()))
                .addValue("accountableBusinessLineUserId", line[ACC_BUSINESS_USERID.getPosition()].trim());
//                .addValue("ebsMiddleOfficeUserId", line[EBS_MIDDLEOFFICE_USERID.getPosition()].trim())
//                .addValue("dataSourcesDescription", line[DATASOURCE_DESCRIPTION.getPosition()].replaceAll("\\s+", " ").trim())
//                .addValue("upstreamDependencyDescription", line[UPSTREAM_DEPENDENCY_DESC.getPosition()].replaceAll("\\s+", " ").trim())
//                .addValue("downstreamImpactDescription", line[DOWNSTREAM_IMPACT_DESC.getPosition()].replaceAll("\\s+", " ").trim())
//                .addValue("fsImpactId", fsImpact.get(line[FS_IMPACT.getPosition()].trim()))
//                .addValue("expectedEntryWorkdayId", entryWorkDay.get(line[EXPECTED_ENTRY_WORKDAY.getPosition()].trim()))
//                .addValue("entryWorkdayTime", line[EXPECTED_ENTRY_TIME.getPosition()].trim())
//                .addValue("intercompanyIndicatorId", interCompanyIndicator.get(line[INTERCOMPANY_INDICATOR.getPosition()].trim()))
//                .addValue("cogsImpacting", line[COGS_IMPACTING.getPosition()].trim())
//                .addValue("soxControlNumber", line[SOX_CONTROL_NO.getPosition()].trim())
//                .addValue("automationToolId", automationTool.get(line[AUTOMATION_TOOL.getPosition()].trim()))
//                .addValue("eliminationNotes", line[ELIMINATION_NOTES.getPosition()].replaceAll("\\s+", " ").trim())
//                .addValue("automationNotes", line[AUTOMATION_NOTES.getPosition()].replaceAll("\\s+", " ").trim())
//                .addValue("status", line[STATUS.getPosition()].trim())
//                .addValue("reasonForInactive", line[INACTIVE_REASON.getPosition()].replaceAll("\\s+", " ").trim())
//                .addValue("inactivatedDate", line[INACTIVATE_DATE.getPosition()].trim())
//                .addValue("modification", line[ACTION.getPosition()].trim())
//                .addValue("segment", line[SEGMENT.getPosition()].trim())
//                .addValue("dataSources", line[DATASOURCE.getPosition()].trim())
//                .addValue("upstreamDependency", line[UPSTREAM_DEPENDENCY.getPosition()].trim())
//                .addValue("downstreamImpact", line[DOWNSTREAM_IMPACT.getPosition()].trim())
//                .addValue("expectedPeriod", line[EXPECTED_PERIOD.getPosition()].trim())
//                .addValue("glAccount", line[GLACCOUNT.getPosition()].trim())
//                .addValue("companyCode", line[COMPANY_CODE.getPosition()].trim())
//                .addValue("centerDepartment", line[DEPARTMENT.getPosition()].trim())
//                .addValue("functionalArea", line[FUNCTIONAL_AREA.getPosition()].trim());
    }

    private SqlParameterSource getModifiedParameters(String[] line, int requestNo) {

        ZoneId zoneId = ZoneId.of("America/Chicago");
        LocalDateTime dateTimeFlag = LocalDateTime.now(zoneId);

        return new MapSqlParameterSource()
                .addValue("requestNo", requestNo)
                .addValue("creationDate", dateTimeFlag)
                .addValue("identifier", Integer.valueOf(line[JOURNAL_IDENTIFIER.getPosition()].trim()))
                .addValue("title", line[TITLE.getPosition()].replaceAll("\\s+", " ").trim())
                .addValue("targetLedgerSystemId", line[TARGET_LEDGER_SYSTEM_ID.getPosition()].trim())
                .addValue("countryId", line[COUNTRY.getPosition()].trim())
                .addValue("processingTeamId", line[PROCESSING_TEAM.getPosition()].trim())
                .addValue("categoryId", line[JOURNAL_ENTRY_CATEGORY.getPosition()].trim())
                .addValue("natureId", line[JOURNAL_ENTRY_NATURE.getPosition()].trim())
                .addValue("methodId", line[JOURNAL_ENTRY_METHOD.getPosition()].trim())
                .addValue("description", line[DESCRIPTION.getPosition()].replaceAll("\\s+", " ").trim())
                .addValue("frequencyId", line[FREQUENCY.getPosition()].trim())
                .addValue("preparerUserId", line[JOURNAL_PREPARER_USERID.getPosition()].trim())
                .addValue("approverUserId", line[JOURNAL_APPROVER_USERID.getPosition()].trim())
                .addValue("reviewerUserId", line[JOURNAL_REVIEWER_USERID.getPosition()].trim())
                .addValue("accountableBusinessLineId", line[ACC_BUSINESS_LINE.getPosition()].trim())
                .addValue("accountableBusinessLineUserId", line[ACC_BUSINESS_USERID.getPosition()].trim());
//                .addValue("ebsMiddleOfficeUserId", line[EBS_MIDDLEOFFICE_USERID.getPosition()].trim())
//                .addValue("dataSourcesDescription", line[DATASOURCE_DESCRIPTION.getPosition()].replaceAll("\\s+", " ").trim())
//                .addValue("upstreamDependencyDescription", line[UPSTREAM_DEPENDENCY_DESC.getPosition()].replaceAll("\\s+", " ").trim())
//                .addValue("downstreamImpactDescription", line[DOWNSTREAM_IMPACT_DESC.getPosition()].replaceAll("\\s+", " ").trim())
//                .addValue("fsImpactId", line[FS_IMPACT.getPosition()].trim())
//                .addValue("expectedEntryWorkdayId", entryWorkDay.get(line[EXPECTED_ENTRY_WORKDAY.getPosition()].trim()))
//                .addValue("entryWorkdayTime", line[EXPECTED_ENTRY_TIME.getPosition()].trim())
//                .addValue("intercompanyIndicatorId", line[INTERCOMPANY_INDICATOR.getPosition()].trim())
//                .addValue("cogsImpacting", line[COGS_IMPACTING.getPosition()].trim())
//                .addValue("soxControlNumber", line[SOX_CONTROL_NO.getPosition()].trim())
//                .addValue("automationToolId", line[AUTOMATION_TOOL.getPosition()].trim())
//                .addValue("eliminationNotes", line[ELIMINATION_NOTES.getPosition()].replaceAll("\\s+", " ").trim())
//                .addValue("automationNotes", line[AUTOMATION_NOTES.getPosition()].replaceAll("\\s+", " ").trim())
//                .addValue("status", line[STATUS.getPosition()].trim())
//                .addValue("reasonForInactive", line[INACTIVE_REASON.getPosition()].replaceAll("\\s+", " ").trim())
//                .addValue("inactivatedDate", line[INACTIVATE_DATE.getPosition()].trim())
//                .addValue("modification", line[ACTION.getPosition()].trim())
//                .addValue("segment", line[SEGMENT.getPosition()].trim())
//                .addValue("dataSources", line[DATASOURCE.getPosition()].trim())
//                .addValue("upstreamDependency", line[UPSTREAM_DEPENDENCY.getPosition()].trim())
//                .addValue("downstreamImpact", line[DOWNSTREAM_IMPACT.getPosition()].trim())
//                .addValue("expectedPeriod", line[EXPECTED_PERIOD.getPosition()].trim())
//                .addValue("glAccount", line[GLACCOUNT.getPosition()].trim())
//                .addValue("companyCode", line[COMPANY_CODE.getPosition()].trim())
//                .addValue("centerDepartment", line[DEPARTMENT.getPosition()].trim())
//                .addValue("functionalArea", line[FUNCTIONAL_AREA.getPosition()].trim());
    }

    public void collectData(JournalEntry journalEntry, String[] line) {
//        if (StringUtils.hasLength(line[SEGMENT.getPosition()]))
//            journalEntry.getSegment().add(this.segment.get(line[SEGMENT.getPosition()].trim()));
//
//        if (StringUtils.hasLength(line[UPSTREAM_DEPENDENCY.getPosition()]))
//            journalEntry.getUpStreamDependency().add(this.upStreamDependency.get(line[UPSTREAM_DEPENDENCY.getPosition()].trim()));
//
//        if (StringUtils.hasLength(line[DOWNSTREAM_IMPACT.getPosition()]))
//            journalEntry.getDownStreamImpact().add(this.downStreamImpact.get(line[DOWNSTREAM_IMPACT.getPosition()].trim()));
//
//        if (StringUtils.hasLength(line[DATASOURCE.getPosition()]))
//            journalEntry.getDataSource().add(this.dataSource.get(line[DATASOURCE.getPosition()].trim()));
//
//        if (StringUtils.hasLength(line[EXPECTED_PERIOD.getPosition()]))
//            journalEntry.getExpectedPeriod().add(Integer.valueOf(line[EXPECTED_PERIOD.getPosition()].trim()));
//
//        if (StringUtils.hasLength(line[GLACCOUNT.getPosition()]))
//            journalEntry.getGlAccount().add(line[GLACCOUNT.getPosition()].trim());
//
//        if (StringUtils.hasLength(line[COMPANY_CODE.getPosition()]))
//            journalEntry.getCompanyCode().add(line[COMPANY_CODE.getPosition()].trim());
//
//        if (StringUtils.hasLength(line[DEPARTMENT.getPosition()]))
//            journalEntry.getDepartment().add(line[DEPARTMENT.getPosition()].trim());
//
//        if (StringUtils.hasLength(line[FUNCTIONAL_AREA.getPosition()]))
//            journalEntry.getFunctionalArea().add(line[FUNCTIONAL_AREA.getPosition()].trim());
//    }

    }
}

