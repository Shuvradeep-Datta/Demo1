package com.deep.demo.service;


import com.deep.demo.dto.*;
import com.deep.demo.repository.JournalRepository;
import com.deep.demo.util.ValidationUtil;
import com.opencsv.CSVWriter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections.map.MultiValueMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static com.deep.demo.constants.JournalEntryConstants.*;
import static com.deep.demo.constants.JournalQueriesConstants.*;


@Service
@Slf4j
@RequiredArgsConstructor
public class JournalEntryService {

    private final JournalRepository journalRepository;
    private final ValidationUtil validateUtil;
    private final UploadJournalService uploadService;
    @Value("${file.tmp.path}")
    private String tempFilePath;

    public PagingResponse getJournals(JournalSearchDto journalSearchDto, Integer pageNumber, Integer pageSize) {
        Page<JournalEntryResponse> pagedResult = getJournalEntryResponsesList(journalSearchDto, pageNumber, pageSize);
        return PagingResponse.builder()
                .totalItems(pagedResult.getTotalElements())
                .journals(pagedResult.getContent())
                .currentPage(pagedResult.getNumber())
                .pageSize(pagedResult.getSize())
                .totalPages(pagedResult.getTotalPages())
                .build();
    }

    private Page<JournalEntryResponse> getJournalEntryResponsesList(JournalSearchDto journalSearchDto, Integer pageNumber, Integer pageSize) {
        PageRequest pageable;
        int count = journalRepository.getJournalEntryCount();
        if (pageNumber != null && pageSize != null)
            pageable = PageRequest.of(pageNumber, pageSize);
        else
            pageable = PageRequest.of(0, count);

        Page<JournalEntryResponse> journalEntryResponsePage;
        if(journalSearchDto != null)
            journalEntryResponsePage = journalRepository.findAll(journalSearchDto, pageable);
        else
            journalEntryResponsePage = journalRepository.findAll(pageable, count);

        List<JournalEntryResponse> journalEntryResponses = journalEntryResponsePage.getContent();
        setJournalEntryResponses(journalEntryResponses);
        return journalEntryResponsePage;
    }


    public List<JournalEntryResponse> getJournalById(Integer journalId) throws JournalEntryNotFoundException {
        Map<String, Number> parameters = Map.of("journalId",journalId);
        List<JournalEntryResponse> journalEntryResponses = journalRepository.getJournalEntryById(parameters);
        if (journalEntryResponses.isEmpty()) {
            throw new JournalEntryNotFoundException("No entry found with Journal ID: " + journalId);
        }
        setJournalEntryResponses(journalEntryResponses);
        return journalEntryResponses;
    }

    public void setJournalEntryResponses(List<JournalEntryResponse> journalEntryResponses) {
        MultiValueMap segmentResponses = journalRepository.getJournalEntryMap(SEGMENT, "segment");
        MultiValueMap expectedPeriodResponses = journalRepository.getJournalEntryMapForInteger(EXPECTED_PERIOD, "expected_period");
        MultiValueMap centerDepartmentResponses = journalRepository.getJournalEntryMap(CENTER_DEPARTMENT, "center_department");
        MultiValueMap dataSourcesResponses = journalRepository.getJournalEntryMap(DATA_SOURCE, "data_sources");
        MultiValueMap downstreamImpactResponses = journalRepository.getJournalEntryMap(DOWNSTREAM_IMPACT, "downstream_impact");
        MultiValueMap functionalAreaResponses = journalRepository.getJournalEntryMap(FUNCTIONAL_AREA, "functional_area");
        MultiValueMap glAccountResponses = journalRepository.getJournalEntryMap(GL_ACCOUNT, "gl_account");
        MultiValueMap upstreamDependencyResponses = journalRepository.getJournalEntryMap(UPSTREAM_DEPENDENCY, "upstream_dependency");
        MultiValueMap companyCodeResponses = journalRepository.getJournalEntryMap(COMPANY_CODE, "company_code");

        mapToJournalEntry(journalEntryResponses,companyCodeResponses,segmentResponses,expectedPeriodResponses,
                centerDepartmentResponses,dataSourcesResponses,downstreamImpactResponses,functionalAreaResponses,
                glAccountResponses,upstreamDependencyResponses);
    }

    private void mapToJournalEntry(List<JournalEntryResponse> journalEntryResponses, MultiValueMap companyCodeResponses, MultiValueMap segmentResponses, MultiValueMap expectedPeriodResponses, MultiValueMap centerDepartmentResponses, MultiValueMap dataSourcesResponses, MultiValueMap downstreamImpactResponses, MultiValueMap functionalAreaResponses, MultiValueMap glAccountResponses, MultiValueMap upstreamDependencyResponses) {
    }

    File getTargetFile(String reportName) {
        return Paths.get(tempFilePath +reportName + System.currentTimeMillis() + CSV).toFile();
    }

    public boolean isEmpty(String[] line) {
        return Arrays.stream(line).filter(l->l.length()>0).findFirst().isEmpty();
    }

    boolean validateHeader(String[] header) {
        return Arrays.equals(header, SUCCESS_REPORT_HEADER.split(","),(h1,h2)->h1.trim().compareToIgnoreCase(h2.trim()));
    }

    void writeJournals(JournalUploadResponse response, List<JournalEntry> journals, CSVWriter successReportWriter, CSVWriter errorReportWriter) {
        successReportWriter.writeNext(LINE.split(","));
        errorReportWriter.writeNext(LINE.split(","));
        successReportWriter.writeNext(SUCCESS_REPORT_HEADER.split(","));
        errorReportWriter.writeNext(ERROR_REPORT_HEADER.split(","));
        int sCount = 0,eCount = 0;
        for (JournalEntry line : journals) {
            if (!line.isError()) {
                successReportWriter.writeAll(line.getJournalIds());
                sCount++;
            } else {
                errorReportWriter.writeAll(line.getJournalIds());
                eCount++;
            }
        }
        response.setSuccessCount(sCount);
        response.setErrorCount(eCount);
    }

    public void saveAllJournals(List<JournalEntry> journals, int requestNo, String source, boolean isRequestInfoSame) {
        journals.parallelStream().forEach(line -> {
            try {
                if (!line.isError()) {
                    uploadService.saveJournal(line, requestNo, source, isRequestInfoSame);
                    uploadService.saveJournalInModificationTable(line, requestNo);
                    uploadService.saveJournalExpectedPeriod(line.getJournalId(), line.getExpectedPeriod());
                    uploadService.saveJournalSegment(line.getJournalId(), line.getSegment());
                    uploadService.saveJournalDataSources(line.getJournalId(), line.getDataSource());
                    uploadService.saveJournalUpstreamDependency(line.getJournalId(), line.getUpStreamDependency());
                    uploadService.saveJournalDownStreamImpact(line.getJournalId(), line.getDownStreamImpact());
                    uploadService.saveJournalGlAccount(line.getJournalId(), line.getGlAccount());
                    uploadService.saveJournalFunctionalArea(line.getJournalId(), line.getFunctionalArea());
                    uploadService.saveJournalDepartment(line.getJournalId(), line.getDepartment());
                    uploadService.saveJournalCompanyCode(line.getJournalId(), line.getCompanyCode());
                }
            } catch (Exception e) {
                log.error("Exception occurred :" + e.getMessage());
                line.getJournalIds().get(0)[41] = parseErrorMessage(e.getMessage());
                line.setError(true);
            }
        });
    }

    private String parseErrorMessage(String message) {
        return message.contains("duplicate") ? message.substring(message.lastIndexOf("Cannot")) : message;
    }

    public File getFile(String fileName) throws FileNotFoundException {
        Path path = Paths.get(tempFilePath + fileName);
        if (Files.notExists(path)) {
            throw new FileNotFoundException("File not found");
        }
        return path.toFile();
    }

    public void deleteFile(String fileName) {
        try {
            Files.deleteIfExists(Paths.get(tempFilePath + fileName));
            log.info("File deleted: {}", fileName);
        } catch (IOException e) {
            log.error("IOException occurred for the file: {}", fileName);
        }
    }

    public File getDownloadCatalogFile(JournalSearchDto journalSearchDto, Integer pageNumber, Integer pageSize, Integer searchText) throws FileNotFoundException, JournalEntryNotFoundException {
        List<JournalEntryResponse> journalEntryResponseList;
        if(searchText != null) {
            log.info("-----Inside getDownloadCatalogFile() where the searchText != null -----");
            journalEntryResponseList = getJournalById(searchText);
        } else {
            log.info("-----Inside getDownloadCatalogFile() where the searchText == null -----");
            journalEntryResponseList = getJournalEntryResponsesList(journalSearchDto,pageNumber, pageSize).getContent();
        }
        return downloadCatalogCSV(journalEntryResponseList, tempFilePath);
    }

    private File downloadCatalogCSV(List<JournalEntryResponse> journalEntryResponseList, String tempFilePath) {
        return null;
    }
}

