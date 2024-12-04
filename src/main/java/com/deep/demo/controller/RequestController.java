package com.deep.demo.controller;


import com.deep.demo.dto.*;
import com.deep.demo.exception.JournalEntryExceptions;
import com.deep.demo.service.FetchAllUsers;
import com.deep.demo.service.FileUploadService;
import com.deep.demo.service.JournalEntryNotFoundException;
import com.deep.demo.service.RequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin
@Slf4j
@RequestMapping(value = "/v1")
public class RequestController {
    private final RequestService requestService;
    private final FetchAllUsers fetchAllUsers;
    private final FileUploadService fileUploadService;
    private static final Logger LOGGER = LoggerFactory.getLogger(RequestController.class);

    @GetMapping("/getRequest")
    public List<RequestDto> getRequests() {
        return requestService.getRequestList();
    }

    @GetMapping("/getlatestRequestNo")
    public int getRequestNo() {
        return requestService.getLatestRequest();
    }

    @GetMapping("/getMyRequestByStatus")
    public List<RecordResponse> getRequestByStatus(@RequestParam("userId") String userId, @RequestParam("status") String status) throws JournalEntryNotFoundException {
        return requestService.getRequestByStatusAndUpdate(userId, status);
    }

    @GetMapping("/getJournalInfoByRequestNo")
    public List<RecordInfoResponse> getJournalInfoByRequestNo(@RequestParam("requestNo") String requestNo) {
        return requestService.getJournalInfoAndUpdate(requestNo);
    }

    @GetMapping("/email")
    public void email(@RequestParam("requestNo") String requestNo, String requestType, String status,
                      @RequestParam(value = "recordInfoResponse", required = false) List<RecordInfoResponse> recordInfoResponse) {
        requestService.sendEmail(requestNo, requestType, status, recordInfoResponse);
    }

    @GetMapping("/getApproveRequestByStatus")
    public List<RecordResponseAdmin> getApproveRequestByStatus( @RequestParam("approverUser") String approverUser, @RequestParam("status") String status)
            throws JournalEntryExceptions {
        return requestService.getApproveRequestByStatus(approverUser, status);
    }

    @GetMapping("/getApproveRequestByUserByStatus")
    public List<RecordResponseAdmin> getApproveRequestByUserByStatus( @RequestParam("approverUser") String approverUser) throws JournalEntryExceptions {
        return requestService.getApproveRequestByUserByStatus(approverUser);
    }

    @GetMapping("/getAdminRequestByStatus")
    public List<RecordResponseAdmin> getAdminRequestByStatus( @RequestParam("status") String status) throws JournalEntryExceptions {
        return requestService.getAdminRequestByStatus(status);
    }

    @GetMapping("/getMjeByRequestNo")
    public List<tempJournalResponse> getMjeByRequestNo( @RequestParam("requestNo") String requestNo) throws JournalEntryExceptions {
        return requestService.getMjeByRequestNo(requestNo);
    }

    @GetMapping("/tempJournals")
    public PagingResponseModifiedJournal getJournalEntries(
            @RequestParam(required = false) Integer pageNumber,
            @RequestParam(required = false) Integer pageSize,
            @RequestParam("requestNo") String requestNo) {
        return requestService.getJournals(requestNo, pageNumber, pageSize);
    }

    @GetMapping("/deleteRecordByRequest")
    public String deleteRecordByRequest( @RequestParam("requestNo") String requestNo) throws JournalEntryExceptions {
        try {
            List<RecordInfoResponse> records = requestService.getRequestsByRequestNumber(requestNo);
            requestService.deleteRecordByRequest(requestNo);
            records.get(0).setStatus("Deleted");
            email(null, null,null, records);
            return "Deleted Succesfully";
        }
        catch (Exception e){
            return e.toString();
        }
    }

    @PostMapping(value = "/createRecord",consumes = { MediaType.APPLICATION_JSON_VALUE,MediaType.MULTIPART_FORM_DATA_VALUE })
    public JournalUploadResponse addRequest(@RequestPart RequestUploadDto requestDtoList, @RequestPart("MJEfile") MultipartFile mjeFile, @RequestPart(value ="AJEfile", required=false) MultipartFile ajeFile) throws Exception {
        if (mjeFile.isEmpty() || !mjeFile.getOriginalFilename().endsWith(".csv")) {
            throw new Exception("Upload file should be a csv file");
        }
        log.info("Creating a record"+mjeFile.getOriginalFilename());
        JournalUploadResponse response = requestService.addRequest(requestDtoList.getRequestData(),mjeFile,ajeFile);
        if(response.getSuccessCount() > 0) {
            email(String.valueOf(response.getRequestNo()), null, null, null);
        }
        return response;
    }

    @PostMapping(value = "/modifyRecordByRequest",consumes = { MediaType.APPLICATION_JSON_VALUE,MediaType.MULTIPART_FORM_DATA_VALUE })
    public JournalUploadResponse modifyRecordByRequest(@RequestPart RequestModifyDto modifyRequestDto,@RequestPart("MJEfile") MultipartFile mjeFile, @RequestPart(value ="AJEfile", required=false) MultipartFile ajeFile) throws Exception {

        if (mjeFile.isEmpty() || !mjeFile.getOriginalFilename().endsWith(".csv")) {
            throw new Exception("Upload file should be a csv file");
        }
        JournalUploadResponse response = requestService.modifyRequest(modifyRequestDto.getRequestModifyData(), mjeFile, ajeFile);
        if(response.getSuccessCount() > 0) {
            email(String.valueOf(response.getRequestNo()), null,null, null);
        }
        return response;
    }

    @PostMapping(value = "/modifyJournals",consumes = { MediaType.APPLICATION_JSON_VALUE,MediaType.MULTIPART_FORM_DATA_VALUE })
    public JournalUploadResponse modifyJournals(@RequestPart RequestUploadDto requestDtoList,@RequestPart("MJEfile") MultipartFile mjeFile, @RequestPart(value ="AJEfile", required=false) MultipartFile ajeFile) throws Exception {
        if (mjeFile.isEmpty() || !mjeFile.getOriginalFilename().endsWith(".csv")) {
            throw new Exception("Upload file should be a csv file");
        }
        JournalUploadResponse response = requestService.modifyJournals(requestDtoList.getRequestData(),mjeFile,ajeFile);
        ;
        if(response.getSuccessCount() > 0) {
            email(String.valueOf(response.getRequestNo()), "Modify",null, null);
        }
        return response;
    }

    @PostMapping("/postComment")
    public void postComment(@RequestBody CommentUploadDto commentDtoList) throws Exception {
        requestService.postComment(new ArrayList<>());
    }

    @PostMapping("/reviewRequestReject")
    public void postRejectRequest(@RequestBody ApprovalUploadDto approvalDtoList) throws Exception {
        requestService.postReviewReject(approvalDtoList.getApprovalData());
        String requestNo = String.valueOf(approvalDtoList.getApprovalData().get(0).getRequestNo());
        email(requestNo, null, null, null);
    }

    @PostMapping("/reviewRequestApprove")
    public void postApproveRequest(@RequestBody ApprovalUploadDto approvalDtoList) throws Exception {
        String status = requestService.postReviewApprove(approvalDtoList.getApprovalData());
        String requestNo = String.valueOf(approvalDtoList.getApprovalData().get(0).getRequestNo());
        email(requestNo, null, status, null);
    }

    @GetMapping("/getCommentByRequestNo")
    public List<CommentDto> getCommentsByRequestNo( @RequestParam("requestNo") String requestNo) throws JournalEntryExceptions {
        return requestService.getCommentByRequestNo(requestNo);
    }

    @GetMapping("/fetchUserList")
    public String fetchUserList(@RequestParam("adGroup") String adGroup ) throws JournalEntryExceptions {
        return fetchAllUsers.getAttributesForGroup(adGroup);
    }

    @GetMapping(value = "/downloadfile", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Resource> downloadfile(@RequestParam("requestNo") String requestNo, @RequestParam("JEType") String jeType) {
        log.info("FileStorageController.downloadfile() start ");
        byte[] docByteArray = null;
        ByteArrayResource resource = null;
        String fileName = requestService.getFileName(requestNo,jeType);

        try {
            docByteArray = fileUploadService.loadfilefromURI(requestNo,fileName);
            resource = new ByteArrayResource(docByteArray);
        } catch (Exception ex) {
            log.error("FileStorageController.downloadfile() error ", ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resource);
        }
        log.info("FileStorageController.downloadfile() end");
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM).contentLength(docByteArray.length)
                .header(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.attachment()
                        .filename(fileName)
                        .build().toString())
                .body(resource);
    }

    @PostMapping(value = "/adminReviewer")
    public void adminReviewer(@RequestPart AdminRevUploadDto adminDtoList) throws Exception {
        requestService.adminReviewUpdate(adminDtoList.getAdminData());
        email(adminDtoList.getAdminData().get(0).getRequestNo(), null, null, null);
    }
}

