package com.deep.demo.service;

import com.deep.demo.dto.*;
import com.deep.demo.repository.JournalRepository;
import com.deep.demo.repository.RequestRepository;
import com.deep.demo.util.ValidationUtil;
import com.microsoft.azure.storage.StorageErrorCode;
import com.microsoft.azure.storage.StorageException;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static com.deep.demo.constants.JournalEntryConstants.ERROR_REPORT;
import static com.deep.demo.constants.JournalEntryConstants.SUCCESS_REPORT;


@Service
@Slf4j
@RequiredArgsConstructor
public class RequestService {
    private final RequestRepository requestRepository;
    private final JournalRepository journalRepository;
    private final FileUploadService fileUploadService;
    private final JournalEntryService journalEntryService;
    private final UploadJournalService uploadService;
    private final ValidationUtil validateUtil;
    private final EmailService emailService;
    private final FetchAllUsers fetchAllUsers;

    private static final Logger LOGGER = LoggerFactory.getLogger(RequestService.class);
    private static final String[] USER_LIST = {"GL-MJEMS-Approver-non-prod", "GL-MJEMS-DataSteward-non-prod", "GL-MJEMS-Admin-non-prod", "GL-MJEMS-Certifier-non-prod"};

    public List<RequestDto> getRequestList() {
        try {
            return requestRepository.getRequests();
        } catch (Exception e) {
            LOGGER.error("Exception occurred while getting request list: " + e.getMessage(), e);
            throw new RuntimeException("Exception occurred while getting request list", e);
        }
    }

    public List<RecordInfoResponse> getRequestsByRequestNumber(String requestNo) {
        try {
            return requestRepository.getRequestsByRequestNumber(requestNo);
        } catch (Exception e) {
            LOGGER.error("Exception occurred while getting requests by request number: " + e.getMessage(), e);
            throw new RuntimeException("Exception occurred while getting requests by request number", e);
        }
    }

    public int getLatestRequest() {
        try {
            return requestRepository.getRequestNo();
        } catch (Exception e) {
            LOGGER.error("Exception occurred while getting latest request: " + e.getMessage(), e);
            throw new RuntimeException("Exception occurred while getting latest request", e);
        }
    }

    public List<RecordResponse> getRequestByStatus(String userId, String status) throws JournalEntryNotFoundException {
        try {
            Map<String, String> parameters = new HashMap<>();
            parameters.put("status", status);
            parameters.put("userId", userId);
            List<RecordResponse> recordResponses = requestRepository.getRequestByStatus(parameters);
            if (recordResponses.isEmpty()) {
                throw new JournalEntryNotFoundException("No " + status + " requests found for " + userId);
            }
            return recordResponses;
        } catch (JournalEntryNotFoundException e) {
            LOGGER.error("Exception occurred: " + e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            LOGGER.error("Exception occurred while getting request by status: " + e.getMessage(), e);
            throw new RuntimeException("Exception occurred while getting request by status", e);
        }
    }

    public String getFileName(String requestNo, String fileType) {
        try {
            fileType = fileType.toUpperCase();
            Map<String, String> parameters = new HashMap<>();
            parameters.put("requestNo", requestNo);
            return requestRepository.getFileName(requestNo, fileType);
        } catch (Exception e) {
            LOGGER.error("Exception occurred while getting file name: " + e.getMessage(), e);
            throw new RuntimeException("Exception occurred while getting file name", e);
        }
    }

    public List<RecordResponseAdmin> getApproveRequestByStatus(String approverUser, String status) {
        try {
            Map<String, String> parameters = new HashMap<>();
            parameters.put("status", status);
            parameters.put("approverUser", approverUser);
            return requestRepository.getApproveRequestByStatus(parameters);
        } catch (Exception e) {
            LOGGER.error("Exception occurred while getting approved requests by status: " + e.getMessage(), e);
            throw new RuntimeException("Exception occurred while getting approved requests by status", e);
        }
    }

    public List<RecordResponseAdmin> getApproveRequestByUserByStatus(String approverUser) {
        try {
            Map<String, String> parameters = new HashMap<>();
            parameters.put("approverUser", approverUser);
            List<RecordResponseAdmin> recordResponses = requestRepository.getApproveRequestByUserByStatus(parameters);
            if (recordResponses.isEmpty()) {
                throw new JournalEntryNotFoundException("No approved requests found for " + approverUser);
            }
            return recordResponses;
        } catch (Exception | JournalEntryNotFoundException e) {
            LOGGER.error("Exception occurred while getting approved requests by user status: " + e.getMessage(), e);
            throw new RuntimeException("Exception occurred while getting approved requests by user status", e);
        }
    }

    public List<RecordResponseAdmin> getAdminRequestByStatus(String status) {
        try {
            Map<String, String> parameters = Map.of("status", status);
            List<RecordResponseAdmin> recordResponses = requestRepository.getAdminRequestByStatus(parameters);
            if (recordResponses.isEmpty()) {
                throw new JournalEntryNotFoundException("No " + status + " requests found");
            }
            return recordResponses;
        } catch (Exception | JournalEntryNotFoundException e) {
            LOGGER.error("Exception occurred while getting admin requests by status: " + e.getMessage(), e);
            throw new RuntimeException("Exception occurred while getting admin requests by status", e);
        }
    }

    public List<tempJournalResponse> getMjeByRequestNo(String requestNo) {
        try {
            Map<String, String> parameters = Map.of("requestNo", requestNo);
            List<tempJournalResponse> mjeResponses = requestRepository.getMjeByRequestNo(parameters);
            if (mjeResponses.isEmpty()) {
                throw new JournalEntryNotFoundException("No data found for Request No: " + requestNo);
            }
            return mjeResponses;
        } catch (Exception | JournalEntryNotFoundException e) {
            LOGGER.error("Exception occurred while getting MJE by request number: " + e.getMessage(), e);
            throw new RuntimeException("Exception occurred while getting MJE by request number", e);
        }
    }

    public PagingResponseModifiedJournal getJournals(String requestNo, Integer pageNumber, Integer pageSize) {
        try {
            Page<tempJournalResponse> pagedResult = getJournalEntryResponsesList(requestNo, pageNumber, pageSize);
            return PagingResponseModifiedJournal.builder()
                    .totalItems(pagedResult.getTotalElements())
                    .journals(pagedResult.getContent())
                    .currentPage(pagedResult.getNumber())
                    .pageSize(pagedResult.getSize())
                    .totalPages(pagedResult.getTotalPages())
                    .build();
        } catch (Exception e) {
            LOGGER.error("Exception occurred while getting journals: " + e.getMessage(), e);
            throw new RuntimeException("Exception occurred while getting journals", e);
        }
    }

    private Page<tempJournalResponse> getJournalEntryResponsesList(String requestNo, Integer pageNumber, Integer pageSize) {
        try {
            PageRequest pageable;
            int count = requestRepository.getTempJournalEntryCount();
            if (pageNumber != null && pageSize != null)
                pageable = PageRequest.of(pageNumber, pageSize);
            else
                pageable = PageRequest.of(0, count);

            return requestRepository.findAll(requestNo, pageable, count);
        } catch (Exception e) {
            LOGGER.error("Exception occurred while getting journal entry response list: " + e.getMessage(), e);
            throw new RuntimeException("Exception occurred while getting journal entry response list", e);
        }
    }

    public List<CommentDto> getCommentByRequestNo(String requestNo) {
        try {
            Map<String, String> parameters = Map.of("requestNo", requestNo);
            return requestRepository.getCommentByRequestNo(parameters);
        } catch (Exception e) {
            LOGGER.error("Exception occurred while getting comments by request number: " + e.getMessage(), e);
            throw new RuntimeException("Exception occurred while getting comments by request number", e);
        }
    }

    public List<RecordInfoResponse> getJournalInfoByRequestNo(String requestNo) {
        try {
            Map<String, String> parameters = Map.of("requestNo", requestNo);
            List<RecordInfoResponse> mjeResponses = requestRepository.getJournalInfoByRequestNo(parameters);
            if (mjeResponses.isEmpty()) {
                throw new JournalEntryNotFoundException("No data found for Request No: " + requestNo);
            }
            return mjeResponses;
        } catch (Exception e) {
            LOGGER.error("Exception occurred while getting journal info by request number: " + e.getMessage(), e);
            throw new RuntimeException("Exception occurred while getting journal info by request number", e);
        } catch (JournalEntryNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteRecordByRequest(String requestNo) {
        try {
            requestRepository.deleteTempByRequest(requestNo);
            requestRepository.deleteRecordByRequest(requestNo);
        } catch (Exception e) {
            LOGGER.error("Exception occurred while deleting record by request number: " + e.getMessage(), e);
            throw new RuntimeException("Exception occurred while deleting record by request number", e);
        }
    }

    public void deleteRecordByOnlyRequest(String requestNo) {
        try {
            requestRepository.deleteRecordByRequest(requestNo);
        } catch (Exception e) {
            LOGGER.error("Exception occurred while deleting record by request number only: " + e.getMessage(), e);
            throw new RuntimeException("Exception occurred while deleting record by request number only", e);
        }
    }

    public void postComment(List<CommentDto> commentUploadDto) {
        try {
            requestRepository.postComment(commentUploadDto);
        } catch (Exception e) {
            LOGGER.error("Exception occurred while posting comment: " + e.getMessage(), e);
            throw new RuntimeException("Exception occurred while posting comment", e);
        }
    }

    public void postReviewReject(List<ApprovalDto> approvalUploadDto) {
        try {
            requestRepository.postRejection(approvalUploadDto);
        } catch (Exception e) {
            LOGGER.error("Exception occurred while posting review rejection: " + e.getMessage(), e);
            throw new RuntimeException("Exception occurred while posting review rejection", e);
        }
    }

    public void adminReviewUpdate(List<AdminRevDto> adminUploadDto) {
        try {
            requestRepository.adminReviewUpdate(adminUploadDto);
        } catch (Exception e) {
            LOGGER.error("Exception occurred while updating admin review: " + e.getMessage(), e);
            throw new RuntimeException("Exception occurred while updating admin review", e);
        }
    }

    public String postReviewApprove(List<ApprovalDto> approvalUploadDto) {
        try {
            String requestNo = String.valueOf(approvalUploadDto.get(0).getRequestNo());
            String approver1 = requestRepository.getApproverByRequestNo(requestNo).get(0);
            if (approver1.equals("N/A")) {
                requestRepository.updateApprover1Flag(requestNo);
            }
            String status = requestRepository.postApproval(approvalUploadDto);

            if (!status.equals("Approved")) return status;
            List<tempJournalResponse> tempJournalResponses = getMjeByRequestNo(requestNo);

            for (tempJournalResponse journalEntry : tempJournalResponses) {
                if (journalEntry.getModification().equals("I")) {
                    requestRepository.postJournalEntry(journalEntry);
                } else if (journalEntry.getModification().equals("U")) {
                    requestRepository.updateJournalEntry(journalEntry);
                }
                processJournalEntry(journalEntry);
            }
            return status;
        } catch (Exception e) {
            LOGGER.error("Exception occurred while approving review: " + e.getMessage(), e);
            throw new RuntimeException("Exception occurred while approving review", e);
        }
    }

    private void processJournalEntry(tempJournalResponse journalEntry) {
        try {
            journalRepository.deleteDataSourceById(journalEntry.getIdentifier());
            journalRepository.saveDataSourceById(journalEntry.getIdentifier(), convertToIntList(journalEntry.getDataSources()));
            journalRepository.deleteSegmentById(journalEntry.getIdentifier());
            journalRepository.saveSegmentById(journalEntry.getIdentifier(), convertToIntList(journalEntry.getSegment()));
            journalRepository.deleteDownStreamImpactById(journalEntry.getIdentifier());
            journalRepository.saveDownstreamImpactById(journalEntry.getIdentifier(), convertToIntList(journalEntry.getDownstreamImpact()));
            journalRepository.deleteUpstreamDependencyById(journalEntry.getIdentifier());
            journalRepository.saveUpstreamDependencyById(journalEntry.getIdentifier(), convertToIntList(journalEntry.getUpstreamDependency()));
            journalRepository.deleteExpectedPeriodById(journalEntry.getIdentifier());
            journalRepository.saveExpectedPeriodById(journalEntry.getIdentifier(), convertToIntList(journalEntry.getExpectedPeriod()));
            journalRepository.deleteCompanyCodeById(journalEntry.getIdentifier());
            journalRepository.saveCompanyCodeById(journalEntry.getIdentifier(), journalEntry.getCompanyCode());
            journalRepository.deleteGlAccountById(journalEntry.getIdentifier());
            journalRepository.saveGlAccountById(journalEntry.getIdentifier(), journalEntry.getGlAccount());
            journalRepository.deleteFunctionalAreaById(journalEntry.getIdentifier());
            journalRepository.saveFunctionalAreaById(journalEntry.getIdentifier(), journalEntry.getFunctionalArea());
            journalRepository.deleteCenterDepartmentById(journalEntry.getIdentifier());
            journalRepository.saveCenterDepartmentById(journalEntry.getIdentifier(), journalEntry.getCenterDepartment());
        } catch (Exception e) {
            LOGGER.error("Exception occurred while processing journal entry: " + e.getMessage(), e);
            deleteJournals(journalEntry.getIdentifier());
            throw new RuntimeException("Exception occurred while processing journal entry", e);
        }
    }

    private void deleteJournals(Integer journalId) {
        journalRepository.deleteDataSourceById(journalId);
        journalRepository.deleteSegmentById(journalId);
        journalRepository.deleteDownStreamImpactById(journalId);
        journalRepository.deleteUpstreamDependencyById(journalId);
        journalRepository.deleteExpectedPeriodById(journalId);
        journalRepository.deleteCompanyCodeById(journalId);
        journalRepository.deleteGlAccountById(journalId);
        journalRepository.deleteFunctionalAreaById(journalId);
        journalRepository.deleteCenterDepartmentById(journalId);
        journalRepository.deleteJournalById(journalId);
    }

    private List<Integer> convertToIntList(List<String> list) {
        try {
            return list.stream()
                    .filter(s -> !s.isEmpty())
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            LOGGER.error("Exception occurred while converting list to integers: " + e.getMessage(), e);
            throw new RuntimeException("Exception occurred while converting list to integers", e);
        }
    }

    public JournalUploadResponse addRequest(List<RequestDto> requestUploadDto, MultipartFile mjeFile, MultipartFile ajeFile) {
        JournalUploadResponse response = new JournalUploadResponse();
        try {
            requestRepository.addRequest(requestUploadDto, mjeFile, ajeFile);
            LOGGER.info("File to db", mjeFile);
            int requestNo = requestRepository.getRequestNo();
            LOGGER.info("RequestNo", requestNo);
            response = uploadFile(mjeFile, requestNo, "create", false);
            LOGGER.info("Response", response.toString());
            int errorCount = response.getErrorCount();
            if (errorCount != 0) {
                LOGGER.info("ErrorCount:" + errorCount + uploadFile(mjeFile, requestNo, "create", false) + uploadFile(mjeFile, requestNo, "create", false));
                deleteRecordByOnlyRequest(Integer.toString(requestNo));
            } else {
                LOGGER.info("MJE file", "MJE_" + requestNo);
                LOGGER.info("Uploading to blob", mjeFile.getOriginalFilename() + requestNo);
                fileUploadService.fileUpload(mjeFile, requestNo);
                LOGGER.info("Uploading done to blob", mjeFile.getOriginalFilename() + requestNo);
                if (ajeFile != null) {
                    fileUploadService.fileUpload(ajeFile, requestNo);
                }
            }
        } catch (Exception e) {
            LOGGER.error("Exception occurred while adding request: " + e.getMessage(), e);
        }
        return response;
    }

    public JournalUploadResponse modifyJournals(List<RequestDto> requestUploadDto, MultipartFile mjeFile, MultipartFile ajeFile) throws Exception {
        JournalUploadResponse response = new JournalUploadResponse();
        try {
            requestUploadDto.get(0).setApproverUser1("N/A");
            requestUploadDto.get(0).setApproverGroup1("N/A");
            requestRepository.addRequest(requestUploadDto, mjeFile, ajeFile);
            LOGGER.info("File to db", mjeFile);
            int requestNo = requestRepository.getRequestNo();
            LOGGER.info("RequestNo", requestNo);
            response = uploadFile(mjeFile, requestNo, "modifyJournal", false);
            LOGGER.info("Response", response.toString());
            int errorCount = response.getErrorCount();
            if (errorCount != 0) {
                LOGGER.info("ErrorCount:" + errorCount + uploadFile(mjeFile, requestNo, "modifyJournal", false) + uploadFile(mjeFile, requestNo, "modifyRequest", false));
                deleteRecordByOnlyRequest(Integer.toString(requestNo));
            } else {
                LOGGER.info("MJE file", "MJE_" + requestNo);
                LOGGER.info("Uploading to blob", mjeFile.getOriginalFilename() + requestNo);
                fileUploadService.fileUpload(mjeFile, requestNo);
                LOGGER.info("Uploading done to blob", mjeFile.getOriginalFilename() + requestNo);
                if (ajeFile != null) {
                    fileUploadService.fileUpload(ajeFile, requestNo);
                }
            }
        } catch (IOException e) {
            LOGGER.error("Exception occurred while modifying journals: " + e.getMessage(), e);
        } catch (Exception e) {
            LOGGER.error("Unexpected error occurred: " + e.getMessage(), e);
        }
        return response;
    }

    public JournalUploadResponse modifyRequest(List<ModifyRequestDto> requestModifyDto, MultipartFile mjeFile, MultipartFile ajeFile) throws Exception {
        JournalUploadResponse response = new JournalUploadResponse();
        try {
            RecordInfoResponse record = requestRepository.getRequestsByRequestNumber(requestModifyDto.get(0).getRequestNo()).get(0);
            boolean isRequestInfoSame = compareCSVFiles(mjeFile, getFileByRequest(String.valueOf(record.getRequestNo()), "mje"));
            String requestNo = requestModifyDto.get(0).getRequestNo();
            String reason = requestModifyDto.get(0).getReason();
            String ajeFileName;
            String requestorName = requestModifyDto.get(0).getRequestorName();
            String country = requestModifyDto.get(0).getCountry();
            String userId = requestModifyDto.get(0).getUserId();

            String approverGroup1 = requestModifyDto.get(0).getApproverGroup1();
            String approverUser1 = requestModifyDto.get(0).getApproverUser1();
            String approverGroup2 = requestModifyDto.get(0).getApproverGroup2();
            String approverUser2 = requestModifyDto.get(0).getApproverUser2();
            String requestType = "Modify";
            String status = "Pending";

            LocalDate dateFlag = LocalDate.now();
            String mjeFileName = mjeFile.getOriginalFilename();
            if (ajeFile != null) {
                ajeFileName = ajeFile.getOriginalFilename();
            } else ajeFileName = null;

            Map<String, String> parameters = new HashMap<>();
            parameters.put("requestNo", requestNo);
            parameters.put("requestorName", requestorName);
            parameters.put("country", country);
            parameters.put("userId", userId);
            parameters.put("requestType", requestType);
            parameters.put("status", status);
            parameters.put("postDate", dateFlag.toString());
            parameters.put("approverGroup1", approverGroup1);
            parameters.put("approverUser1", approverUser1);
            parameters.put("approverGroup2", approverGroup2);
            parameters.put("approverUser2", approverUser2);
            parameters.put("reason", reason);
            parameters.put("mjeFile", mjeFileName);
            parameters.put("ajeFile", ajeFileName);
            response = uploadFile(mjeFile, Integer.parseInt(requestNo), "modifyRequest", isRequestInfoSame);
            int errorCount = response.getErrorCount();

            if (errorCount == 0) {
                fileUploadService.fileUpload(mjeFile, Integer.parseInt(requestNo));
                requestRepository.modifyRequest(parameters);
                if (ajeFile != null) {
                    fileUploadService.fileUpload(ajeFile, Integer.parseInt(requestNo));
                }
            }
        } catch (Exception e) {
            LOGGER.error("Error", e);
        }
        return response;
    }

    public boolean compareCSVFiles(MultipartFile file1, MultipartFile file2) throws IOException {
        Reader reader1 = new InputStreamReader(file1.getInputStream());
        Reader reader2 = new InputStreamReader(file2.getInputStream());
        Iterable<CSVRecord> records1 = CSVFormat.DEFAULT.parse(reader1);
        Iterable<CSVRecord> records2 = CSVFormat.DEFAULT.parse(reader2);
        for (CSVRecord record1 : records1) {
            if (!records2.iterator().hasNext()) {
                return false;
            }
            CSVRecord record2 = records2.iterator().next();

            for (int i = 0; i < record1.size(); i++) {
                if (!record1.get(i).equals(record2.get(i))) {
                    return false;
                }
            }
        }
        if (records2.iterator().hasNext()) {
            return false;
        }
        return true;
    }

    private MultipartFile getFileByRequest(String requestNo, String jeType) throws Exception {
        log.info("FileStorageController.downloadfile() start ");
        byte[] docByteArray = null;
        String fileName = getFileName(requestNo, jeType);
        try {
            docByteArray = fileUploadService.loadfilefromURI(requestNo, fileName);
        } catch (Exception ex) {
            log.error("FileStorageController.downloadfile() error ", ex);
            throw new Exception("Unable to get file details for request No :" + requestNo);
        }
        log.info("FileStorageController.downloadfile() end");
        Path tempFile = Files.createTempFile(fileName, ".csv");
        Files.write(tempFile, docByteArray);
        System.out.println("Temp file : " + tempFile);
        File file = tempFile.toFile();
        FileSystemResource fileSystemResource = new FileSystemResource(file);
        MultipartFile multipartFile = new MultipartFile() {
            @Override
            public String getName() {
                return fileName;
            }

            @Override
            public String getOriginalFilename() {
                return fileName;
            }

            @Override
            public String getContentType() {
                return "text/csv";
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public long getSize() {
                return file.length();
            }

            @Override
            public byte[] getBytes() throws IOException {
                return Files.readAllBytes(file.toPath());
            }

            @Override
            public InputStream getInputStream() throws IOException {
                return fileSystemResource.getInputStream();
            }

            @Override
            public void transferTo(File dest) throws IOException, IllegalStateException {
                fileSystemResource.getFile().renameTo(dest);
            }
        };
        file.deleteOnExit();
        return multipartFile;
    }

    public JournalUploadResponse uploadFile(MultipartFile csvFile, int requestNo, String source, boolean isRequestInfoSame) {
        File successReport = journalEntryService.getTargetFile(SUCCESS_REPORT);
        File errorReport = journalEntryService.getTargetFile(ERROR_REPORT);
        CSVWriter successReportWriter = null;
        CSVWriter errorReportWriter = null;
        JournalUploadResponse response = new JournalUploadResponse();
        response.setRequestNo(requestNo);
        try {
            successReportWriter = new CSVWriter(new FileWriter(successReport));
            errorReportWriter = new CSVWriter(new FileWriter(errorReport));
            CSVReader csvReader = new CSVReader(new InputStreamReader(csvFile.getInputStream(), StandardCharsets.UTF_8));
            csvReader.skip(1);

            if (!journalEntryService.validateHeader(csvReader.readNext()))
                throw new RuntimeException("Header Validation failed. Please upload a valid Csv");

            uploadService.getAdminConfigTables();
            List<JournalEntry> journals = new ArrayList<>();
            JournalEntry journal = null;
            for (String[] line : csvReader) {
                if (journalEntryService.isEmpty(line)) continue;
                if (!line[0].isEmpty()) {
                    if (journal != null)
                        journals.add(journal);
                    String errorMessage = validateUtil.validateFields(line);
                    journal = JournalEntry.builder().line(line).journalId(line[1]).build();
                    journal.getJournalIds().add(journal.addLine(line, errorMessage));
                } else {
                    String errorMessage = validateUtil.validateMultiLineFields(journal, line);
                    journal.getJournalIds().add(journal.addLine(line, errorMessage));
                }
                if (!journal.isError()) uploadService.collectData(journal, line);
            }
            if (journal != null)
                journals.add(journal);
            journalEntryService.saveAllJournals(journals, requestNo, source, isRequestInfoSame);
            journalEntryService.writeJournals(response, journals, successReportWriter, errorReportWriter);
            if (response.getErrorCount() != 0) response.setErrorReport(errorReport.getName());
            else errorReport.delete();
            if (response.getSuccessCount() != 0) response.setSuccessReport(successReport.getName());
            else successReport.delete();
        } catch (IOException | CsvValidationException e) {
            LOGGER.error("Error occurred during file upload: ", e);
            throw new RuntimeException(e);
        } catch (RuntimeException e) {
            LOGGER.error("Runtime error occurred: ", e);
            throw e;
        } finally {
            if (successReportWriter != null) {
                try {
                    successReportWriter.close();
                } catch (IOException e) {
                    LOGGER.error("Error when closing successReportWriter", e);
                }
            }
            if (errorReportWriter != null) {
                try {
                    errorReportWriter.close();
                } catch (IOException e) {
                    LOGGER.error("Error when closing errorReportWriter", e);
                }
            }
        }
        return response;
    }

    public Map<String, String> parseUserList(String userList) {
        if (userList == null || userList.isEmpty()) {
            throw new IllegalArgumentException("UserList must not be null or empty");
        }
        Map<String, String> map = new HashMap<>();
        userList = userList.substring(1, userList.length() - 1);
        String[] users = userList.split(", ");

        for (String user : users) {
            String[] parts = user.split(" - ");
            if (parts.length == 2) {
                map.put(parts[1], parts[0]);
            } else {
                throw new IllegalArgumentException("User data is in incorrect format");
            }
        }
        return map;
    }

    public List<RecordResponse> getRequestByStatusAndUpdate(String userId, String status) throws JournalEntryNotFoundException {
        validate(userId, status);
        Map<String, String> map = getUserMap();
        List<RecordResponse> responses = getRequestByStatus(userId, status);
        updateApproverUser(responses, map);
        return responses;
    }

    public List<RecordInfoResponse> getJournalInfoAndUpdate(String requestNo) {
        List<RecordInfoResponse> responses = getJournalInfoByRequestNo(requestNo);
        Map<String, String> map = getUserMap();
        updateApproverUserForRecordInfoResponse(responses, map); // Call the new method
        return responses;
    }
    public String sendEmail(String requestNo, String requestType, String status, List<RecordInfoResponse> recordInfoResponse) {
        File attachment = downloadFileHelper(requestNo);
        if(recordInfoResponse != null) {
            emailService.sendMail(recordInfoResponse, null, attachment);
        } else {
            List<RecordInfoResponse> responses = getRequestsByRequestNumber(requestNo);
            if(requestType != null) {
                responses.get(0).setRequestType(requestType);
                responses.get(0).setApproverGroup1("N/A");
                responses.get(0).setApproverUser1("N/A");
            }
            emailService.sendMail(responses, status, attachment);
        }
        if(attachment != null && attachment.delete()){
            LOGGER.info("Attachment file deleted after sending email: " + attachment.getName());
        }
        return "OK";
    }

    public File downloadFileHelper(String requestNo) {
        try {
            String fileName = getFileName(requestNo, "AJE");
            if(fileName != null && !fileName.trim().isEmpty()) {
                byte[] docByteArray = fileUploadService.loadfilefromURI(requestNo, fileName);
                if (docByteArray == null || docByteArray.length == 0) {
                    return null;
                }
                File file = new File(fileName);
                Files.write(file.toPath(), docByteArray, StandardOpenOption.CREATE);
                return file;
            }
            throw new Exception();
        } catch (StorageException ex) {
            if (ex.getErrorCode().equals(StorageErrorCode.BLOB_NOT_FOUND)) {
                return null;
            }
            LOGGER.info("Error creating AJE attachment: " + ex.getMessage());
            return null;
        } catch (Exception ex) {
            LOGGER.info("Error creating AJE attachment: " + ex.getMessage());
            return null;
        }
    }

    private void validate(String userId, String status) {
        if (userId == null || userId.isEmpty() || status == null || status.isEmpty()) {
            throw new IllegalArgumentException("UserId and status must not be null or empty");
        }
    }

    public Map<String, String> getUserMap() {
        Map<String, String> userMap = new HashMap<>();
        for (String user : USER_LIST) {
            userMap.putAll(parseUserList(fetchAllUsers.getAttributesForGroup(user)));
        }
        userMap.put("LB-mje-approver1", "LB-mje-approver1");
        userMap.put("LB-mje-approver", "LB-mje-approver");
        userMap.put("LB-mje-approver2", "LB-mje-approver2");
        return userMap;
    }

    private void updateApproverUser(List<? extends RecordResponse> responses, Map<String, String> map) {
        for (RecordResponse response : responses) {
            String approver1 = response.getApproverUser1();
            String approver2 = response.getApproverUser2();
            if (approver1 != null && !approver1.isEmpty()) {
                updateApprover(approver1, map, response::setApproverUser1);
            }
            if (approver2 != null && !approver2.isEmpty()) {
                updateApprover(approver2, map, response::setApproverUser2);
            }
        }
    }

    private void updateApproverUserForRecordInfoResponse(List<RecordInfoResponse> responses, Map<String, String> map) {
        for (RecordInfoResponse response : responses) {
            String approver1 = response.getApproverUser1();
            String approver2 = response.getApproverUser2();
            if (approver1 != null && !approver1.isEmpty()) {
                updateApprover(approver1, map, response::setApproverUser1);
            }
            if (approver2 != null && !approver2.isEmpty()) {
                updateApprover(approver2, map, response::setApproverUser2);
            }
        }
    }

    private void updateApprover(String approver, Map<String, String> map, Consumer<String> setter) {
        String mapValue = map.get(approver);
        setter.accept(Objects.requireNonNullElse(mapValue, "N/A"));
    }


}