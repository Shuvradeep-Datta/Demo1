package com.deep.demo.service;


import com.deep.demo.dto.*;
import com.deep.demo.repository.JournalRepository;
import com.deep.demo.util.ValidationUtil;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.deep.demo.constants.JournalEntryConstants.*;
import static com.deep.demo.enums.CSVHeaders.SEGMENT;
import static org.springframework.util.ObjectUtils.isEmpty;

@Service
@Slf4j
@RequiredArgsConstructor
public class JournalEntryService {
    private  final JournalRepository journalRepository;
    private final UploadJournalService uploadJournalService;
    private final ValidationUtil validationUtil;

    @Value("${file.tmp.path}")
    private String tempFilePath;

    public void deleteFile(String fileName){
        try{
            Files.deleteIfExists(Paths.get(tempFilePath+fileName));
            log.info("File deleted: {} ",fileName);
        } catch (IOException e) {
            log.error("IoException Occured for the file: {}", fileName);
        }
    }

    public File getFile(String fileName) throws FileNotFoundException{
        Path path =Paths.get(tempFilePath + fileName);
        if(Files.notExists(path)){
            throw new FileNotFoundException("File not Found");

        }
        return path.toFile();
    }


    public PagingResponse getJournals(JournalSearchDto journalSearchDto,Integer pageNumber, Integer pageSize){
        Page<JournalEntryResponse>pagedResult =getJournalEntryResponseList(journalSearchDto,pageNumber,pageSize);
        return PagingResponse.builder()
                .totalItems(pagedResult.getTotalElements())
                .journals(pagedResult.getContent())
                .currentPage(pagedResult.getNumber()).pageSize(pagedResult.getSize()).totalPages(pagedResult.getTotalPages()).build();
    }

    private Page<JournalEntryResponse> getJournalEntryResponseList(JournalSearchDto journalSearchDto, Integer pageNumber, Integer pageSize) {
        PageRequest pageable;
        int count = journalRepository.getJournalEntryCount();
        if (pageNumber != null && pageSize != null)
                pageable= PageRequest.of(pageNumber,pageSize);
        else
            pageable =PageRequest.of(0,count);

        Page <JournalEntryResponse> journalEntryResponsePage;

        if(journalSearchDto != null)
            journalEntryResponsePage =journalRepository.findAll(journalSearchDto, pageable);
        else
            journalEntryResponsePage = journalRepository.findAll(pageable, count);

        List<JournalEntryResponse> journalEntryResponses =journalEntryResponsePage.getContent();


//        setJournalEntryResponses(journalEntryResponses);

        return journalEntryResponsePage;

    }


    private void mapToJournalEntry(List<JournalEntryResponse> journalEntryResponses, MultiValueMap companyCodeResponses, MultiValueMap segmentResponses, MultiValueMap expectedPeriodResponses, MultiValueMap centerDepartmentResponses, MultiValueMap dataSourcesResponses, MultiValueMap downstreamImpactResponses, MultiValueMap functionalAreaResponses, MultiValueMap glAccountResponses, MultiValueMap upstreamDependencyResponses) {
    }



    public void saveAllJournals(List<JournalEntry> journals){
        journals.parallelStream().forEach(line->{
            try{
                if(!line.isError())
                    uploadJournalService.saveJournal(line);
            }catch (DataAccessException | CsvValidationException e){
                log.error("Exception occured :"+e.getMessage());
                line.getJournalIds().get(0)[41]=parseErrorMessage(e.getMessage());
                line.setError(true);
            }
        });
    }

    public String parseErrorMessage(String message){
        return message.contains("duplicate")?message.substring(message.lastIndexOf("Cannot")):message;

    }



public List<JournalEntryResponse> getJournalById(Integer journalId) throws JournalEntryNotFoundException {
    Map<String ,Number> parameters =Map.of("journalId",journalId);
    List<JournalEntryResponse> journalEntryResponses = journalRepository.getJournalEntryById(parameters);
    if (journalEntryResponses.isEmpty()){
        throw new JournalEntryNotFoundException(""+journalId);

    }
//    setJournalEntryResponses(journalEntryResponses);
    return journalEntryResponses;
}

public File getDownloadCatalogFile(JournalSearchDto journalSearchDto, Integer pageNumber,Integer pageSize,Integer searchText) throws JournalEntryNotFoundException {
        List<JournalEntryResponse> journalEntryResponseList;
        if(searchText !=null){
            log.info("------------------------");
            journalEntryResponseList =getJournalById(searchText);
            
        }else {
            log.info("----------------------");
            journalEntryResponseList =getJournalEntryResponseList(journalSearchDto,pageNumber,pageSize).getContent();
            
        }
        return downloadCatalogCSV(journalEntryResponseList,tempFilePath);
}

    private File downloadCatalogCSV(List<JournalEntryResponse> journalEntryResponseList, String tempFilePath) {
        return  null;
    }

    public boolean isEmpty(String[] line){
        return Arrays.stream(line).filter(l->l.length()>0).findFirst().isEmpty();
    }

    public boolean validatHeader(String [] header){
        return Arrays.equals(header,SUCCESS_REPORT_HEADER.split(""),(h1,h2)->h1.trim().compareToIgnoreCase(h2.trim()));
    }

//    public JournalUploadResponse uploadFile(MultipartFile csvFile) throws IOException, CsvValidationException {
//        File successReport = getTargetFile(SUCCESS_REPORT);
//        File errorReport = getTargetFile(ERROR_REPORT);
//        CSVWriter successReportWriter = new CSVWriter(new FileWriter(successReport));
//        CSVWriter errorReportWriter = new CSVWriter(new FileWriter(errorReport));
//        JournalUploadResponse response = new JournalUploadResponse();
//        try(CSVReader csvReader = new CSVReader(new InputStreamReader(csvFile.getInputStream(),StandardCharsets.UTF_8))) {
//            csvReader.skip(1);
//
//            if (!validatHeader(csvReader.readNext()))
//                    throw new CsvValidationException("Header Validation failed. please upload a valid Csv");
//            uploadJournalService.getAdminConfigTables();
//            List<JournalEntry> journals =new ArrayList<>();
//            JournalEntry journal = null;
//
//
//            if (response.getErrorCount()!=0) response.setErrorReport(errorReport.getName());
//            else errorReport.delete();
//            if (response.getSuccessCount() !=0) response.setSuccessReport(successReport.getName());
//            else successReport.delete();
//
//        }finally {
//            successReportWriter.close();
//            errorReportWriter.close();
//        }
//        return response;
//    }

    public void writeJournals(JournalUploadResponse response, List<JournalEntry> journals, CSVWriter successReportWriter, CSVWriter errorReportWriter) {
    successReportWriter.writeNext(LINE.split(","));
    errorReportWriter.writeNext(LINE.split(","));
    successReportWriter.writeNext(SUCCESS_REPORT_HEADER.split(""));
    errorReportWriter.writeNext(ERROR_REPORT_HEADER.split(","));
    int sCount = 0, eCount = 0;
    for (JournalEntry line:journals){
        if (!line.isError()){
            successReportWriter.writeAll(line.getJournalIds());
            sCount++;

        }else {
            errorReportWriter.writeAll(line.getJournalIds());
            eCount++;

        }
    }
    response.setSuccessCount(sCount);
    response.setErrorCount(eCount);
    }


    public File getTargetFile(String reportName){
        return Paths.get(tempFilePath+reportName+System.currentTimeMillis()+CSV).toFile();
    }

}
